package org.textway.tools.converter.spec

class SLookahead implements SExpression {

    Set<SReference> terms;
    boolean inverted;

    void accept(SVisitor visitor) {
    }
}
