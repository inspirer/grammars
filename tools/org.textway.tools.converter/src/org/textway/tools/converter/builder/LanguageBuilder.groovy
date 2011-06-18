package org.textway.tools.converter.builder

import org.textway.tools.converter.ConvertException
import org.textway.tools.converter.spec.*

/**
 *  evgeny, 6/18/11
 */
class LanguageBuilder {

    SLanguage language;
    Map<String, SSymbol> symbols = [:];

    List<SSymbol> inputs = [];
    List<SSymbol> delimeters = [];

    LanguageBuilder(SLanguage language) {
        this.language = language
        language.all.each { symbols[it.name] = it; }
    }

    void eliminateRecursionInTerms() {
        for (SSymbol term: language.all.findAll {it.isTerm}) {
            if (!(term.value instanceof SChoice)) continue;

            def rules = ((SChoice) term.value).elements;
            def recursiveRule = null;
            def recursiveCount = 0;
            for (SExpression e: rules) {
                if (refers(e, term)) {
                    recursiveCount++;
                    recursiveRule = e;
                }
            }
            if (recursiveCount < 1) continue;
            if (recursiveCount > 1) {
                // TODO
                throw new ConvertException(recursiveRule.location, "several recursive rules");
            }
            if (!(recursiveRule instanceof SSequence)) {
                throw new ConvertException(recursiveRule.location, "unsupported recursion: should be sequence");
            }

            boolean leftRecursion;
            boolean isOptionalTail;
            List<SExpression> rulesyms = ((SSequence) recursiveRule).elements;
            if (rulesyms.last() instanceof SReference && refers(rulesyms.last(), term)) {
                leftRecursion = false;
                isOptionalTail = ((SReference) rulesyms.last()).isOptional;
                rulesyms = rulesyms[0..-2];
            } else if (rulesyms.first() instanceof SReference && refers(rulesyms.first(), term)) {
                leftRecursion = true;
                isOptionalTail = ((SReference) rulesyms.first()).isOptional;
                rulesyms = rulesyms[1..-1];
            } else {
                throw new ConvertException(recursiveRule.location, "unsupported recursion: should be left or right");
            }
            if (rulesyms.any {refers(it, term)} || rulesyms.isEmpty()) {
                throw new ConvertException(recursiveRule.location, "unsupported recursion: unknown error");
            }

            // refactor
            def newvalue;
            if (rules.size() == 1) {
                if (!isOptionalTail)
                    throw new ConvertException(recursiveRule.location, "unsupported self-recursion");

                SExpression quantifier = SUtil.createQuantifier(SUtil.createSequence(rulesyms, recursiveRule.location), 1, -1);
                term.value = quantifier;

            } else {
                SExpression quantifier = SUtil.createQuantifier(SUtil.createSequence(rulesyms, recursiveRule.location), 0, -1)
                List<SExpression> tt = rules.findAll {it != recursiveRule};
                if (tt.size() < 1) {
                    throw new ConvertException(recursiveRule.location, "unknown error");
                }

                if (tt.size() == 1) {
                    def rule1 = tt.first();
                    if (isOptionalTail) {
                        rule1 = SUtil.createQuantifier(rule1, 0, 1);
                    }
                    if (isEmpty(rule1)) {
                        term.value = quantifier;
                    } else if (!isOptionalTail && equals(rule1, ((SQuantifier) quantifier).inner)) {
                        ((SQuantifier) quantifier).min = 1;
                        term.value = quantifier;
                    } else {
                        term.value = SUtil.createSequence(leftRecursion ? [rule1, quantifier] : [quantifier, rule1], term.location);
                    }
                } else {
                    if (tt.any {isEmpty(it)}) {
                        tt.removeAll {isEmpty(it)};
                        isOptionalTail = true;
                    }
                    if (tt.size() < 1) {
                        term.value = quantifier;
                    } else {
                        def element = tt.size() > 1 ? SUtil.createChoice(tt, tt.first().location) : tt.first();
                        if (isOptionalTail) {
                            element = SUtil.createQuantifier(element, 0, 1);
                        }
                        term.value = SUtil.createSequence(leftRecursion ? [element, quantifier] : [quantifier, element], term.location);
                    }
                }
            }
        }
    }

    void convertLexems() {
        for (SSymbol lexem: language.all.findAll {it.isTerm && it.isEntry}) {
            def builder = new StringBuilder()
            RegexUtil.handleRegexp(lexem.value, builder);
            lexem.value = RegexUtil.create(builder.toString(), lexem.location);
        }
    }

    void markEntryPoints() {
        inputs.each { it.isEntry = true }
        delimeters.each { it.isEntry = true }
        Queue<SSymbol> queue = new LinkedList<SSymbol>();
        queue.addAll(inputs);
        Set<SSymbol> ssym = new HashSet<SSymbol>();
        while (!queue.isEmpty()) {
            SSymbol first = queue.remove();
            assert first.isEntry;
            ssym.clear();
            collectUsed(first.value, ssym);
            for (SSymbol s: ssym) {
                if (!s.isEntry) {
                    s.isEntry = true;
                    if (!s.isTerm) {
                        queue.add(s);
                    }
                }
            }
        }

        for (SSymbol s: language.all) {
            if (!s.isTerm && !s.isEntry) {
                throw new ConvertException(s.location, "non-term ${s.name} is not used");
            }
        }
    }

    private void collectUsed(SExpression expr, Set<SSymbol> result) {
        if (expr instanceof SReference) {
            result.add(expr.resolved);
        } else if (expr instanceof SSequence) {
            expr.elements.each { collectUsed(it, result) }
        } else if (expr instanceof SChoice) {
            expr.elements.each { collectUsed(it, result) }
        } else if (expr instanceof SSetDiff) {
            collectUsed(expr.left, result);
            collectUsed(expr.right, result);
        } else if (expr instanceof SQuantifier) {
            collectUsed(expr.inner, result);
        }
    }

    private boolean refers(SExpression expr, SSymbol sym) {
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

    private boolean equals(SExpression e1, SExpression e2) {
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

    private SExpression unwrap(SExpression e) {
        if(e instanceof SSequence && e.elements != null && e.elements.size() == 1) {
            return e.elements.first()
        }
        if(e instanceof SChoice && e.elements != null && e.elements.size() == 1) {
            return e.elements.first()
        }
        return e;
    }


    private boolean isEmpty(SExpression expr) {
        return expr instanceof SSequence && (((SSequence) expr).elements == null || ((SSequence) expr).elements.isEmpty());
    }

    void prepare(File optionsFile) {
        BuilderOptions opts = new BuilderOptions().read(optionsFile);

        opts.options['input'].each {
            if (!symbols.containsKey(it)) {
                throw new ConvertException(optionsFile.getName(), "cannot resolve input: ${it}");
            }
            SSymbol inp = symbols[it]
            if (inp.isTerm) {
                throw new ConvertException(optionsFile.getName(), "input symbol cannot be term: ${it}");
            }
            inputs.add(inp);
        }
        if (inputs.empty) {
            throw new ConvertException(optionsFile.getName(), "no input symbols defined");
        }

        opts.options['nonTerm'].each {
            if (!symbols.containsKey(it)) {
                throw new ConvertException(optionsFile.getName(), "cannot resolve sym for nonTerm: ${it}");
            }
            SSymbol inp = symbols[it]
            if (!inp.isTerm) {
                throw new ConvertException(optionsFile.getName(), "nonTerm symbol should be term: ${it}");
            }
            inp.isTerm = false;
        }

        opts.options['delimeters'].each {
            if (!symbols.containsKey(it)) {
                throw new ConvertException(optionsFile.getName(), "cannot resolve delimeter: ${it}");
            }
            SSymbol inp = symbols[it]
            if (!inp.isTerm) {
                throw new ConvertException(optionsFile.getName(), "delimeter should be term: ${it}");
            }
            delimeters.add(inp);
        }
        if (delimeters.empty) {
            throw new ConvertException(optionsFile.getName(), "no delimeters defined");
        }
    }
}
