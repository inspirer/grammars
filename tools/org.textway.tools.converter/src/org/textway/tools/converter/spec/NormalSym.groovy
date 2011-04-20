package org.textway.tools.converter.spec

class NormalSym extends Sym {

    def rules = []

    NormalSym(name) {
        super(name)
    }

    @Override
    void accept(String line) {
        if (line =~ /^\[empty\]$/) {
            // empty rule
        } else if (line =~ /^any Unicode code unit$/) {
            // ignore, TODO
        } else if (line =~ /^any character in the Unicode categor(ies|y)/) {
            // ignore, TODO
        } else if (line =~ /^(\w+)\s+but not \S+(or \S+)*/) {
            // ignore, TODO
        } else {
            def rule = line.split(/\s+/)
            rules.add(rule);
        }
    }
}