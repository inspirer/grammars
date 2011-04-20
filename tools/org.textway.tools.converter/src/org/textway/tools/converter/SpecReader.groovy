package org.textway.tools.converter

import org.textway.tools.converter.spec.OneOfSym
import org.textway.tools.converter.spec.NormalSym

class SpecReader {

    List read_lex(String filename) {
        def current = null;
        def result = [];
        def linenum = 0;
        new File(filename).eachLine {
            line ->
            linenum++;
            def matcher = null;
            if (line =~ /^#/) return;

            if (line.trim().isEmpty()) {
                current = null;
            } else if ((matcher = line =~ /^(\w+)\s*::?\s*(one of\s*)?$/)) {
                if (current) { throw new ScriptException("${linenum}: no empty line between definitions") }
                def name = matcher[0][1];
                current = matcher[0][2] != null ? new OneOfSym(name) : new NormalSym(name);
                result.add(current)
            } else if (line =~ /^\t.*$/) {
                if (!current) { throw new ScriptException("${linenum}: unexpected line\n" + line) }
                line = line.trim()
                current.accept(line)
            } else {
                throw new ScriptException("${linenum}: bad line\n${line}");
            }
        }
        return result
    }
}
