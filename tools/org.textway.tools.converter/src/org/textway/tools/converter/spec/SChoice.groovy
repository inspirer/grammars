package org.textway.tools.converter.spec

class SChoice implements SExpression {
    final List<SExpression> elements;

    SChoice(SExpression ... elements) {
        this.elements = elements.toList()
    }

    SChoice(List<? extends SExpression> elements) {
        this.elements = elements
    }

    void accept(SVisitor visitor) {
        for(SExpression e : elements) {
            e.accept(visitor)
        }
    }
}
