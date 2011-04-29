package org.textway.tools.converter.spec

class SCharacter implements SExpression {
    char c

    SCharacter(char c) {
        this.c = c
    }

    void accept(SVisitor visitor) {
    }
}
