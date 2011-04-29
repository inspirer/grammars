package org.textway.tools.converter.spec

class SSequence implements SExpression {
    final List<SExpression> elements;

    SSequence(SExpression ... elements) {
        this.elements = elements.toList()
    }

    SSequence(List<SExpression> elements) {
        this.elements = elements
    }

    void accept(SVisitor visitor) {
        for(SExpression e : elements) {
            e.accept(visitor)
        }
    }
}
