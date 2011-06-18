package org.textway.tools.converter.parser

import org.textway.tools.converter.spec.*

class Resolver {
    SLanguage language;
    Map<String, SSymbol> defined = [:];

    Resolver(SLanguage language) {
        this.language = language
    }

    void resolveAll(List<String> toExpand) {
        defined = [:];
        language.all.each { defined[it.name] = it }

        for (s in toExpand) {
            expand(s);
        }

        for (s in language.all) {
            resolveInExpr(s.value);
        }
    }

    void resolveInExpr(SExpression e) {
        if (e instanceof SReference) {
            resolve(e);
        } else if (e instanceof SSequence) {
            for (x in e.elements) {
                resolveInExpr(x);
            }
        } else if (e instanceof SChoice) {
            for (x in e.elements) {
                resolveInExpr(x);
            }
        } else if (e instanceof SLookahead) {
            for (x in e.terms) {
                resolveInExpr(x);
            }
        } else if (e instanceof SSetDiff) {
            resolveInExpr(e.left);
            resolveInExpr(e.right);
        }
    }

    void expand(String sid) {
        if (defined[sid]) {
            ((SChoice) defined[sid].value).elements.reverse().each {
                def text, location;
                if (it instanceof SReference) {
                    text = it.internalText;
                    location = it.location;
                } else if (it instanceof SCharacter) {
                    text = it.c.toString();
                    location = it.location
                } else {
                    throw new ParseException(defined[sid].location, "cannot expand ${sid}, unknown element: ${it}");
                }
                if (defined[text]) {
                    throw new ParseException(defined[sid].location, "cannot expand ${sid}, ${text} already exists");
                }
                def newsym = SUtil.createSymbol(text, location)
                language.all.add(language.all.indexOf(defined[sid]) + 1, newsym);
                defined[text] = newsym;
                newsym.isTerm = defined[sid].isTerm
                newsym.value =
                    text.length() == 1 ? SUtil.createChar(text.charAt(0), location) :
                        SUtil.createSequence((text.toCharArray().collect { SUtil.createChar(it, location)}), location)
            }
        } else {
            throw new ParseException(0, "cannot expand `${sid}': unknown symbol");
        }
    }

    void resolve(SReference ref) {
        String reference = ref.internalText;
        if (reference.endsWith("opt")) {
            reference = reference[0..-4];
            ref.isOptional = true;
        }

        ref.resolved = defined[reference];
        if (ref.resolved == null) {
            throw new ParseException(ref.location, "unresolved reference: ${ref.internalText}");
            //println "${ref.location}: unresolved ${ref.internalText}";
        }
    }

}
