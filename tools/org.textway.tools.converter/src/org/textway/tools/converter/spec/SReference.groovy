package org.textway.tools.converter.spec

class SReference implements SExpression {
    String text;
    String location;
    SSymbol resolved

    SReference(String text, String location) {
        this.text = text
        this.location = location
    }

    static List<SExpression> create(String[] s, String location) {
        return s.collect { SUtil.create(it, location) }
    }

    void accept(SVisitor visitor) {
        visitor.visit(this);
    }
}
