package org.textway.tools.converter

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

            try {
                new SpecReader().read(lexer, parser);
            } catch(ParseException ex) {
                die(ex.toString());
            }
        }
    }

    static void die(String... message) {
        message.each { println it }
        System.exit(1);
    }
}
