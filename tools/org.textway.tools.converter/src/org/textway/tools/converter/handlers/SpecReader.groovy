package org.textway.tools.converter.handlers

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SReference

import org.textway.tools.converter.spec.SVisitor
import org.textway.tools.converter.spec.SUtil

import org.textway.tools.converter.spec.SCharacter
import org.textway.tools.converter.spec.SSequence
import org.textway.tools.converter.spec.SLanguage

class SpecReader {

    ReaderOptions opts = new ReaderOptions();

    List<SSymbol> read(File file) {
        LineStrategy currentStrategy = null;
        List<SSymbol> result = [];
        def linenum = 0;
        file.eachLine {
            line ->
            linenum++;
            def location = "${file.name},${linenum}";
            def matcher = null;
            if ((matcher = line =~ /^##\s*(.*)\s*$/)) {
                opts.acceptLine((String) matcher[0][1], location);
                return;
            }
            if (line =~ /^#/) return;

            if (line.trim().isEmpty()) {
                currentStrategy = null;
            } else if ((matcher = line =~ /^(\w+)\s*::?\s*(one of\s*)?$/)) {
                if (currentStrategy) { throw new ParseException(linenum, "no empty line between definitions") }
                String name = matcher[0][1];
                boolean isOneOf = matcher[0][2] != null;
                SSymbol s = new SSymbol(name, location);
                currentStrategy = isOneOf ? new OneOfStrategy(s) : new DefaultStrategy(s);
                result.add(s)
            } else if (line =~ /^\t.*$/) {
                if (!currentStrategy) { throw new ParseException(linenum, "unexpected line\n" + line) }
                line = line.trim()
                currentStrategy.accept(line, location)
            } else {
                throw new ParseException(linenum, "bad line\n${line}");
            }
        }
        return result
    }

    void populate(String sid, List<SSymbol> syms, Map<String,SSymbol> defined) {
        if(defined[sid]) {
            ((SChoice)defined[sid].value).elements.each {
              def text, location;
              if(it instanceof SReference) {
                  text = it.text;
                  location = it.location;
              } else if (it instanceof SCharacter) {
                  text = it.c.toString();
                  location = it.location
              } else {
                  throw new ParseException(defined[sid].location, "cannot populate ${sid}, unknown element: ${it}");
              }
              if(defined[text]) {
                  throw new ParseException(defined[sid].location, "cannot populate ${sid}, ${text} already exists");
              }
              def newsym = new SSymbol(text, location)
              syms.add(newsym);
              defined[text] = newsym;
              newsym.isTerm = defined[sid].isTerm
              newsym.value =
                  text.length() == 1 ? new SCharacter(text.charAt(0), location) :
                      new SSequence((text.toCharArray().collect { new SCharacter(it,location)}))
            }
        } else {
            throw new ParseException(0, "cannot populate `${sid}': unknown symbol");
        }
    }

    SLanguage read(File l, File p) {
        def lex = read(l);
        def parser = read(p);

        for(s in lex) {
            s.isTerm = true;
        }

        List<SSymbol> all = [];
        all.addAll(lex);
        all.addAll(parser);

        Map<String, SSymbol> defined = [:];
        all.each { defined[it.name] = it }

        for(s in opts.options['populate']) {
            populate(s, all, defined);
        }

        SVisitor v = new SVisitor() {
            void visit(SReference ref) {
                String reference = ref.text;
                if(reference.endsWith("opt")) {
                    reference = reference[0..-4];
                    ref.isOptional = true;
                }

                ref.resolved = defined[reference];
                if(ref.resolved == null) {
                    throw new ParseException(ref.location, "unresolved reference: ${ref.text}");
                    //println "${ref.location}: unresolved ${ref.text}";
                }
            }

        }
        all.each {
            it.value.accept v
        }

        def inputs = opts.options['input'].collect {
            def sym = defined[it];
            if (sym == null) {
                throw new ParseException(0, "unresolved input symbol: ${it}");
            }
            return sym
        };

        opts.options['nonTerm'].each {
            def sym = defined[it];
            if (sym == null || !sym.isTerm) {
                throw new ParseException(0, "cannot make nonterm: ${it}");
            }
            sym.isTerm = false;
        }

        return new SLanguage(all, inputs)
    }

    interface LineStrategy {
        void accept(String line, String location)
    }

    class OneOfStrategy implements LineStrategy {
        SSymbol s;

        OneOfStrategy(SSymbol s) {
            this.s = s
        }

        void accept(String line, String location) {
            ((SChoice) s.value).elements.addAll(SUtil.create(line.split(/\s+/), location, opts));
        }
    }

    class DefaultStrategy implements LineStrategy {

        SSymbol s

        DefaultStrategy(SSymbol s) {
            this.s = s
        }

        @Override
        void accept(String line, String location) {
            for (st in opts.options['style']) {
                def hndlr = HandlersFactory.getHandler(st);

                if (hndlr instanceof LineHandler) {
                    if(((LineHandler)hndlr).tryHandle(s, line, location, opts)) return;
                } else if(hndlr instanceof LinePartHandler) {
                    line = hndlr.handleParts(line, location, opts)
                } else {
                    throw new ParseException(0, "no handler: ${st}");
                }
            }

            if(!HandlersFactory.getDefault().tryHandle(s, line, location, opts)) {
                throw new ParseException(0, "cannot handle line\n${line}");
            }
        }
    }
}
