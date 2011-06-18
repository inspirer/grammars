package org.textway.tools.converter.parser

import org.textway.tools.converter.ConvertException
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SLanguage
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUtil

class SReader {

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
            if ((matcher = line =~ /^\.\s*(\w+)\s+(.*?)\s*$/)) {
                opts.acceptLine((String) matcher[0][1], (String) matcher[0][2], location);
                return;
            }
            if (line =~ /^#/) return;

            if (line.trim().isEmpty()) {
                currentStrategy = null;
            } else if ((matcher = line =~ /^(\w+)\s*::?\s*(one of\s*)?$/)) {
                if (currentStrategy) { throw new ConvertException(location, "no empty line between definitions") }
                String name = matcher[0][1];
                boolean isOneOf = matcher[0][2] != null;
                SSymbol s = SUtil.createSymbol(name, location);
                s.isTerm = (Boolean) opts.options['lexem'];
                currentStrategy = isOneOf ? new OneOfStrategy(s) : new DefaultStrategy(s);
                result.add(s)
            } else if (line =~ /^\t.*$/) {
                if (!currentStrategy) { throw new ConvertException(location, "unexpected line\n" + line) }
                line = line.trim()
                currentStrategy.accept(line, location)
            } else {
                throw new ConvertException(location, "bad line\n${line}");
            }
        }
        return result
    }

    SLanguage read(String name, File file) {
        def all = read(file);

        def language = new SLanguage()
        language.name = name;
        language.all = all;

        new Resolver(language).resolveAll((List<String>) opts.options['expand']);
        return language;
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
            ((SChoice) s.value).elements.addAll(SReaderUtil.create(line.split(/\s+/), location, opts));
        }
    }

    class DefaultStrategy implements LineStrategy {
        SSymbol s;

        DefaultStrategy(SSymbol s) {
            this.s = s
        }

        @Override
        void accept(String line, String location) {
            for (st in opts.options['style']) {
                def hndlr = HandlersFactory.getHandler(st);

                if (hndlr instanceof LineHandler) {
                    if (((LineHandler) hndlr).tryHandle(s, line, location, opts)) return;
                } else if (hndlr instanceof LinePartHandler) {
                    line = hndlr.handleParts(line, location, opts)
                } else {
                    throw new ConvertException(location, "no handler: ${st}");
                }
            }

            if (!HandlersFactory.getDefault().tryHandle(s, line, location, opts)) {
                throw new ConvertException(location, "cannot handle line\n${line}");
            }
        }
    }
}
