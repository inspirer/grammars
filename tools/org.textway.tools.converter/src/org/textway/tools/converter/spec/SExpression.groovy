package org.textway.tools.converter.spec

interface SExpression {

    void accept(SVisitor visitor);
}
