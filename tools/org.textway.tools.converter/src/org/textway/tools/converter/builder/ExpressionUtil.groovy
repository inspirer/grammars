package org.textway.tools.converter.builder

import org.textway.tools.converter.ConvertException
import org.textway.tools.converter.syntax.SRegexp
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

    static SExpression clone(SExpression expr) {
        if (expr == null) return null;

        if (expr instanceof SAnyChar) {
            return SUtil.createAnyChar(expr.location)
        } else if (expr instanceof SCharacter) {
            return SUtil.createChar(expr.c, expr.location)
        } else if (expr instanceof SChoice) {
            return SUtil.createChoice(expr.elements.collect { clone(it) }, expr.location)
        } else if (expr instanceof SLookahead) {
            return SUtil.createLookahead(expr.terms.collect { clone(expr) }, expr.inverted, expr.location)
        } else if (expr instanceof SNoNewLine) {
            return SUtil.createNoNewLine(expr.location)
        } else if (expr instanceof SQuantifier) {
            return SUtil.createQuantifier(clone(expr.inner), expr.min, expr.max);
        } else if (expr instanceof SReference) {
            def reference = SUtil.createReference(expr.internalText, expr.location)
            reference.resolved = expr.resolved;
            reference.isOptional = expr.isOptional;
            return reference;
        } else if (expr instanceof SSequence) {
            return SUtil.createSequence(expr.elements.collect {clone(it)}, expr.location);
        } else if (expr instanceof SSetDiff) {
            return SUtil.createDiff(clone(expr.left), clone(expr.right), expr.location);
        } else if (expr instanceof SUnicodeCategory) {
            return SUtil.createUnicodeCategory(expr.name, expr.location);
        } else if (expr instanceof SRegexp) {
            return RegexUtil.create(expr.text, expr.location);
        } else {
            throw new ConvertException(expr.location, "Cannot clone: ${expr}");
        }
    }

    static SExpression replaceReference(SExpression expr, SSymbol sym, Closure<SExpression> cl) {
        if (expr instanceof SReference) {
            if (((SReference) expr).resolved == sym) {
                return cl.call(expr);
            }
        } else if (expr instanceof SSequence) {
            for (int i: 0..<expr.elements.size()) {
                expr.elements[i] = replaceReference(expr.elements[i], sym, cl)
            }
        } else if (expr instanceof SChoice) {
            for (int i: 0..<expr.elements.size()) {
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

    static SExpression replaceSetDiff(SExpression expr, Closure<SExpression> cl) {
        if (expr instanceof SSequence) {
            for (int i: 0..<expr.elements.size()) {
                expr.elements[i] = replaceSetDiff(expr.elements[i], cl)
            }
        } else if (expr instanceof SChoice) {
            for (int i: 0..<expr.elements.size()) {
                expr.elements[i] = replaceSetDiff(expr.elements[i], cl)
            }
        } else if (expr instanceof SSetDiff) {
            return cl.call(expr);
        } else if (expr instanceof SQuantifier) {
            expr.inner = replaceSetDiff(expr.inner, cl);
        }
        return expr;
    }

    static boolean collectSymbolsSets(SExpression expr, List<SExpression> plus, List<SExpression> minus, boolean inPlus) {
        expr = unwrap(expr);
        if(expr instanceof SSetDiff) {
            if(!inPlus) {
                throw new ConvertException(expr.location, "cannot process double negation in nested setdiff");
            }
            return collectSymbolsSets(expr.left, plus, minus, true) && collectSymbolsSets(expr.right, plus, minus, false);
        } else if(expr instanceof SReference) {
            return collectSymbolsSets(expr.resolved.value, plus, minus, inPlus);
        } else if(expr instanceof SChoice) {
            return expr.elements.every { collectSymbolsSets(it, plus, minus, inPlus) }
        } else if(expr instanceof SCharacter || expr instanceof SUnicodeCategory) {
            if(inPlus) {
                plus.add(expr);
            } else {
                minus.add(expr);
            }
            return true;
        } else if(expr instanceof SAnyChar) {
            if(!inPlus) {
                throw new ConvertException(expr.location, "[any character] found in negation");
            }
            return true;
        }
        return false;
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
        if (e instanceof SSequence && e.elements != null && e.elements.size() == 1) {
            return e.elements.first()
        }
        if (e instanceof SChoice && e.elements != null && e.elements.size() == 1) {
            return e.elements.first()
        }
        return e;
    }

    static List<SExpression> unwrapSequence(SExpression e) {
        e = unwrap(e);
        if (e instanceof SSequence) {
            return (List) ((SSequence) e).elements.collect { unwrapSequence(it) }.flatten()
        }
        return [e];
    }

    private static void combineQuantifiers(SSequence expr) {
        boolean tryAgain = true;
        while (tryAgain) {
            tryAgain = false;
            int indexToDelete = -1;
            for (int i: 1..<expr.elements.size()) {
                SExpression prev = expr.elements[i - 1];
                SExpression curr = expr.elements[i];
                if (curr instanceof SQuantifier && equals(((SQuantifier) curr).inner, prev)) {
                    indexToDelete = i - 1;
                    curr.min++;
                    if (curr.max != -1) {
                        curr.max++;
                    }
                    break;
                } else if (prev instanceof SQuantifier && equals(((SQuantifier) prev).inner, curr)) {
                    indexToDelete = i;
                    prev.min++;
                    if (prev.max != -1) {
                        prev.max++;
                    }
                }
            }
            if (indexToDelete >= 0) {
                tryAgain = true;
                expr.elements.remove(indexToDelete);
            }
        }
    }

    static SExpression simplify(SExpression expr) {
        if (expr instanceof SSequence) {
            for (int i: 0..<expr.elements.size()) {
                expr.elements[i] = simplify(expr.elements[i])
            }
            if (expr.elements.any {it instanceof SSequence}) {
                expr.elements = expr.elements.collect { it instanceof SSequence ? it.elements : [it] }.flatten().collect {simplify((SExpression) it)};
            }
            if (expr.elements.size() == 1) return expr.elements.first();
            combineQuantifiers(expr)
        } else if (expr instanceof SChoice) {
            for (int i: 0..<expr.elements.size()) {
                expr.elements[i] = simplify(expr.elements[i])
            }
            if (expr.elements.any {it instanceof SChoice}) {
                expr.elements = expr.elements.collect { it instanceof SChoice ? it.elements : [it] }.flatten().collect {simplify((SExpression) it)};
            }
            if (expr.elements.size() == 1) return expr.elements.first();
        } else if (expr instanceof SSetDiff) {
            expr.left = simplify(expr.left);
            expr.right = simplify(expr.right);
        } else if (expr instanceof SQuantifier) {
            if (expr.min == 1 && expr.max == 1) {
                return expr.inner;
            }
            expr.inner = simplify(expr.inner);
        }
        return expr;
    }


    static boolean isEmpty(SExpression expr) {
        return expr instanceof SSequence && (((SSequence) expr).elements == null || ((SSequence) expr).elements.isEmpty());
    }
}
