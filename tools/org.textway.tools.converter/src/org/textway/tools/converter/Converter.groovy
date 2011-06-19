package org.textway.tools.converter

import org.textway.tools.converter.builder.LanguageBuilder
import org.textway.tools.converter.parser.SReader
import org.textway.tools.converter.spec.SLanguage
import org.textway.tools.converter.writer.SWriter

class Converter {

    static final def TITLE = "converter - textway grammar converter";

    public static void main(String[] args) {
        if (args.length >= 1) {
            die(TITLE, "run in the directory with .spec files");
        }

        def filesmap = [] as Set
        new File(".").eachFileMatch(~/^[^\.]*\.spec$/) {
            String name = it.getName();
            filesmap += name.substring(0, name.lastIndexOf('.'));
        }

        def todo = (filesmap as List).sort();
        if (todo.size() < 1) {
            die(TITLE, "no .spec files found");
        }

        for (String prefix in todo) {
            def spec = new File("${prefix}.spec");
            if (!spec.exists()) {
                die("no file");
            }

            SLanguage l;
            try {
                l = new SReader().read(prefix, spec);
                save(l, 0)

                process(l);

            } catch (ConvertException ex) {
                die(ex.toString());
                return;
            }
        }
    }

    static void process(SLanguage lang) {
        def opts = new File("${lang.name}.def");
        if (!opts.exists()) {
            die("no file: ${lang.name}.def");
        }
        LanguageBuilder builder = new LanguageBuilder(lang);
        builder.prepare(opts)
        builder.markEntryPoints()

        save(lang, 1)

        builder.eliminateRecursionInTerms()

        save(lang, 2)

        builder.substituteLexemDefinitions();
        builder.simplifyLexerRules();

        save(lang, 3)

        builder.eliminateRecursionInTerms();
        builder.simplifyLexerRules();

        save(lang, 4)

        builder.substituteSetDiffs();

        save(lang, 5);
    }

    static void save(SLanguage lang, int step) {
        String content = new SWriter(lang, step).write();
        new File("${lang.name}.${step}.spec").write(content);
    }


    static void die(String... message) {
        message.each { println it }
        System.exit(1);
    }
}
