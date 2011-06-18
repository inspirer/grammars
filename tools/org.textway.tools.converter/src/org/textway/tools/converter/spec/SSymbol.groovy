package org.textway.tools.converter.spec

class SSymbol {

    String name;
    String location;
    SExpression value;
    boolean isTerm = false;
    boolean isEntry = false;

    String toString() {
        return "`" + name + "'"
    }
}
