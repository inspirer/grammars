package org.textway.tools.converter.builder

import org.textway.tools.converter.spec.*

/**
 *  evgeny, 6/19/11
 */
class ExpressionUtil {

    static Set<SReference> getReferences(SExpression expr) {
        Set<SReference> refs = new HashSet<SReference>()
        collectReferences(expr, refs);
        return refs;
    }

    static void collectReferences(SExpression expr, Set<SReference> result) {
        if (expr instanceof SReference) {
            result.add(expr);
        } else if (expr instanceof SSequence) {
            expr.elements.each { collectReferences(it, result) }
        } else if (expr instanceof SChoice) {
            expr.elements.each { collectReferences(it, result) }
        } else if (expr instanceof SSetDiff) {
            collectReferences(expr.left, result);
            collectReferences(expr.right, result);
        } else if (expr instanceof SQuantifier) {
            collectReferences(expr.inner, result);
        }
    }

    static Set<SSymbol> getReferencedSymbols(SExpression expr) {
        Set<SSymbol> refs = new HashSet<SSymbol>()
        collectReferencedSymbols(expr, refs);
        return refs;
    }

    static void collectReferencedSymbols(SExpression expr, Set<SSymbol> result) {
        if (expr instanceof SReference) {
            result.add(expr.resolved);
        } else if (expr instanceof SSequence) {
            expr.elements.each { collectReferencedSymbols(it, result) }
        } else if (expr instanceof SChoice) {
            expr.elements.each { collectReferencedSymbols(it, result) }
        } else if (expr instanceof SSetDiff) {
            collectReferencedSymbols(expr.left, result);
            collectReferencedSymbols(expr.right, result);
        } else if (expr instanceof SQuantifier) {
            collectReferencedSymbols(expr.inner, result);
        }
    }

    static boolean refers(SExpression expr, SSymbol sym) {
        if (expr instanceof SReference) {
            return expr.resolved == sym;
        } else if (expr instanceof SSequence) {
            return expr.elements.any { refers(it, sym) }
        } else if (expr instanceof SChoice) {
            return expr.elements.any { refers(it, sym) }
        } else if (expr instanceof SSetDiff) {
            return refers(expr.left, sym) || refers(expr.right, sym);
        } else if (expr instanceof SQuantifier) {
            return refers(expr.inner, sym);
        }
        return false;
    }

    static SExpression replaceReference(SExpression expr, SSymbol sym, Closure<SExpression> cl) {
        if (expr instanceof SReference) {
            if(((SReference)expr).resolved == sym) {
                return cl.call(expr);
            }
        } else if (expr instanceof SSequence) {
            for(int i : 0..<expr.elements.size()) {
                expr.elements[i] = replaceReference(expr.elements[i], sym, cl)
            }
        } else if (expr instanceof SChoice) {
            for(int i : 0..<expr.elements.size()) {
                expr.elements[i] = replaceReference(expr.elements[i], sym, cl)
            }
        } else if (expr instanceof SSetDiff) {
            expr.left = replaceReference(expr.left, sym, cl);
            expr.right = replaceReference(expr.right, sym, cl);
        } else if (expr instanceof SQuantifier) {
            expr.inner = replaceReference(expr.inner, sym, cl);
        }
        return expr;
    }

    static boolean equals(SExpression e1, SExpression e2) {
        e1 = unwrap(e1);
        e2 = unwrap(e2);
        if (e1 instanceof SReference && e2 instanceof SReference) {
            return e1.isOptional == e2.isOptional && e1.resolved == e2.resolved;
        } else if (e1 instanceof SSequence && e2 instanceof SSequence) {
            List<SExpression> elist1 = e1.elements;
            List<SExpression> elist2 = e2.elements;
            if (elist1.size() != elist2.size()) {
                return false;
            }
            for (int i: 0..<elist1.size()) {
                if (!equals(elist1[i], elist2[i])) {
                    return false;
                }
            }
            return true;
        } else if (e1 instanceof SCharacter && e2 instanceof SCharacter) {
            return e1.c == e2.c;
        } else if (e1 instanceof SAnyChar && e2 instanceof SAnyChar) {
            return true;
        } else if (e1 instanceof SQuantifier && e2 instanceof SQuantifier) {
            return e1.min == e2.min && e1.max == e2.max && equals(e1.inner, e2.inner);
        } else if (e1 instanceof SUnicodeCategory && e2 instanceof SUnicodeCategory) {
            return e1.name.equals(e2.name);
        }

        return false;
    }

    static SExpression unwrap(SExpression e) {
        if(e instanceof SSequence && e.elements != null && e.elements.size() == 1) {
            return e.elements.first()
        }
        if(e instanceof SChoice && e.elements != null && e.elements.size() == 1) {
            return e.elements.first()
        }
        return e;
    }


    static boolean isEmpty(SExpression expr) {
        return expr instanceof SSequence && (((SSequence) expr).elements == null || ((SSequence) expr).elements.isEmpty());
    }
}
