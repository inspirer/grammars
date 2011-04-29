package org.textway.tools.converter.spec

class SSymbol {

    String name;
    String location;
    SExpression value = new SChoice();
    boolean isTerm = false;

    SSymbol(String name, String location) {
        this.name = name
        this.location = location;
    }
}
