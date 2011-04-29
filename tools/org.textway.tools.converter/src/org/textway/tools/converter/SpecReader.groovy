package org.textway.tools.converter

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SReference
import org.textway.tools.converter.handlers.HandlersFactory
import org.textway.tools.converter.spec.SVisitor

class SpecReader {

    static List<SSymbol> read(File file) {
        LineStrategy currentStrategy = null;
        Set<String> settings = [];
        List<SSymbol> result = [];
        def linenum = 0;
        file.eachLine {
            line ->
            linenum++;
            def matcher = null;
            if ((matcher = line =~ /^#\s*\[(\w+(\s*,\s*\w+)*)\]\s*$/)) {
                String s = matcher[0][1];
                settings = [];
                s.split(/,/).each { settings.add(it.trim()) }
                return;
            }
            if (line =~ /^#/) return;

            if (line.trim().isEmpty()) {
                currentStrategy = null;
            } else if ((matcher = line =~ /^(\w+)\s*::?\s*(one of\s*)?$/)) {
                if (currentStrategy) { throw new ParseException(linenum, "no empty line between definitions") }
                String name = matcher[0][1];
                boolean isOneOf = matcher[0][2] != null;
                SSymbol s = new SSymbol(name, "${file.name},${linenum}");
                currentStrategy = isOneOf ? new OneOfStrategy(s) : new DefaultStrategy(s, settings);
                result.add(s)
            } else if (line =~ /^\t.*$/) {
                if (!currentStrategy) { throw new ParseException(linenum, "unexpected line\n" + line) }
                line = line.trim()
                currentStrategy.accept(line, "${file.name},${linenum}")
            } else {
                throw new ParseException(linenum, "bad line\n${line}");
            }
        }
        return result
    }

    def read(File l, File p) {
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

        SVisitor v = new SVisitor() {
            void visit(SReference ref) {
                ref.resolved = defined[ref.text];
                if(ref.resolved == null) {
                    throw new ParseException(ref.location, "unresolved reference: ${ref.text}");
                }
            }

        }
        all.each {
            it.value.accept v
        }
    }

    interface LineStrategy {
        void accept(String line, String location)
    }

    static class OneOfStrategy implements LineStrategy {
        SSymbol s;

        OneOfStrategy(SSymbol s) {
            this.s = s
        }

        void accept(String line, String location) {
            ((SChoice) s.value).elements.addAll(SReference.create(line.split(/\s+/), location));
        }
    }

    static class DefaultStrategy implements LineStrategy {

        SSymbol s;
        Set<String> settings = []

        DefaultStrategy(SSymbol s, Set<String> settings) {
            this.s = s
            this.settings = settings;
        }

        @Override
        void accept(String line, String location) {
            for (st in settings) {
                def hndlr = HandlersFactory.getFor(st);
                if (hndlr) {
                    if (hndlr.tryHandle(s, line, location)) {
                        return;
                    }
                } else {
                    throw new ParseException(0, "no handler: ${st}");
                }
            }

            if(!HandlersFactory.getDefault().tryHandle(s, line, location)) {
                throw new ParseException(0, "cannot handle line\n${line}");
            }
        }
    }
}
