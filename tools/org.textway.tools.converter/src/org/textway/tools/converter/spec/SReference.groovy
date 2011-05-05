package org.textway.tools.converter.spec

class SReference extends SExpression {
    /* when resolved */
    SSymbol resolved;
    boolean isOptional = false;

    String internalText;
}
