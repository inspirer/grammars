package org.textway.tools.converter.spec

class SSetDiff implements SExpression {
    final SExpression left;
    final SExpression right;

    SSetDiff(SExpression left, SExpression right) {
        this.left = left;
        this.right = right;
    }

    void accept(SVisitor visitor) {
        left.accept(visitor)
        right.accept(visitor)
    }
}
