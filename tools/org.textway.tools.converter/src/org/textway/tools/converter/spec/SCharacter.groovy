package org.textway.tools.converter.spec

class SCharacter implements SExpression {
    char c
    String location

    SCharacter(char c, String location) {
        this.c = c
        this.location = location
    }

    void accept(SVisitor visitor) {
    }
}
