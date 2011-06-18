package org.textway.tools.converter.parser

import org.textway.tools.converter.spec.SExpression

class PartsRegistry {
    Map<String, SExpression> partid2expr = [:]
    int counter = 1;

    String nextId(SExpression expr) {
        def s = "_____partid" + counter++
        partid2expr[s] = expr;
        return s;
    }
}
