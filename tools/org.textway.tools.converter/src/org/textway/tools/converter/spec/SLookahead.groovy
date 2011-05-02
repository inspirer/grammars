package org.textway.tools.converter.spec

class SLookahead implements SExpression {

    List<SExpression> terms;
    boolean inverted;

    SLookahead(List<SExpression> terms, boolean inverted) {
        this.terms = terms
        this.inverted = inverted
    }

    void accept(SVisitor visitor) {
    }
}
