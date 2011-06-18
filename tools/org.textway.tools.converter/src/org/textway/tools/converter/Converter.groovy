package org.textway.tools.converter

import org.textway.tools.converter.parser.SReader
import org.textway.tools.converter.parser.ParseException
import org.textway.tools.converter.spec.SLanguage

import org.textway.tools.converter.writer.SWriter

class Converter {

    static final def TITLE = "converter - textway grammar converter";

    public static void main(String[] args) {
        if (args.length >= 1) {
            die(TITLE, "run in the directory with .spec files");
        }

        def filesmap = [] as Set
        new File(".").eachFileMatch(~/.*\.spec/) {
            String name = it.getName();
            filesmap += name.substring(0, name.lastIndexOf('.'));
        }

        def todo = (filesmap as List).sort();
        if (todo.size() < 1) {
            die(TITLE, "no .spec files found");
        }

        for(String prefix in todo) {
            def spec = new File("${prefix}.spec");
            if(!spec.exists()) {
                die("no file");
            }

            SLanguage l;
            try {
                l = new SReader().read(prefix, spec);

            } catch(ParseException ex) {
                die(ex.toString());
                return;
            }

            String content = new SWriter(l).write();
            new File("${l.name}.0.spec").write(content);
        }
    }

    static void die(String... message) {
        message.each { println it }
        System.exit(1);
    }
}
