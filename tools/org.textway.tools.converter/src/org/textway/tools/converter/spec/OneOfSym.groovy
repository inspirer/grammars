package org.textway.tools.converter.spec

class OneOfSym extends Sym {
    List list;

    OneOfSym(name) {
        super(name)
        list = []
    }

    void accept(String line) {
        list.addAll(line.split(/\s+/));
    }
}
