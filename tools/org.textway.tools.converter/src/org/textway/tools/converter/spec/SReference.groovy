package org.textway.tools.converter.spec

class SReference implements SExpression {
    String text;
    String location;
    SSymbol resolved

    SReference(String text, String location) {
        this.text = text
        this.location = location
    }

    void accept(SVisitor visitor) {
        visitor.visit(this);
    }
}
