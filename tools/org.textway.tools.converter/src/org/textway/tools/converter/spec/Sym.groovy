package org.textway.tools.converter.spec

abstract class Sym {

    def name;
    def isLexem = false;

    Sym(name) {
        this.name = name
    }

    abstract void accept(String line)
}
