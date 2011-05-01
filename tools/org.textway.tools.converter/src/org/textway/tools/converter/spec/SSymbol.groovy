package org.textway.tools.converter.spec

import org.textway.tools.converter.ParseException

class SSymbol {

    String name;
    String location;
    SExpression value = new SChoice();
    boolean isTerm = false;

    SSymbol(String name, String location) {
        this.name = name
        this.location = location;

        if(name.endsWith("opt")) {
            throw new ParseException(location, "symbol cannot end with opt : ${name}");
        }
    }
}
