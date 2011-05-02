package org.textway.tools.converter

import org.textway.tools.converter.handlers.SpecReader
import org.textway.tools.converter.handlers.ParseException
import org.textway.tools.converter.spec.SLanguage

class Converter {

    static final def TITLE = "converter - textway grammar converter";

    public static void main(String[] args) {
        if (args.length >= 1) {
            die(TITLE, "run in the directory with .spec files");
        }

        def filesmap = [] as Set
        new File(".").eachFileMatch(~/.*_(lexer|parser)\.spec/) {
            String name = it.getName();
            filesmap += name.substring(0, name.lastIndexOf('_'));
        }

        def todo = (filesmap as List).sort();
        if (todo.size() < 1) {
            die(TITLE, "no .spec files found");
        }

        for(def prefix in todo) {
            def lexer = new File("${prefix}_lexer.spec");
            if(!lexer.exists()) {
                die("no lexer .spec");
            }
            def parser = new File("${prefix}_parser.spec");
            if(!parser.exists()) {
                die("no parser .spec");
            }

            SLanguage l;
            try {
                l = new SpecReader().read(lexer, parser);
                l.name = prefix;
                l.detectLexems()

            } catch(ParseException ex) {
                die(ex.toString());
                return;
            }

            store(new File("${l.name}.s"), l)
        }
    }

    private static def store(File file, SLanguage l) {
        PrintWriter pw = file.newPrintWriter()
        pw.println("lang = \"java\"");
        pw.println("prefix = \"${l.name}\"");
        pw.println("\n# Lexer\n");
        pw.close()
    }

    static void die(String... message) {
        message.each { println it }
        System.exit(1);
    }
}
