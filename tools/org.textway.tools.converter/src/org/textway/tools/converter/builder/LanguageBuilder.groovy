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
            eliminateRecursionInTerm(term);
        }
    }

    private void eliminateRecursionInTerm(SSymbol term) {
        if (!(term.value instanceof SChoice)) return;

        def rules = ((SChoice) term.value).elements;
        def recursiveRule = null;
        def recursiveCount = 0;
        for (SExpression e: rules) {
            if (ExpressionUtil.refers(e, term)) {
                recursiveCount++;
                recursiveRule = e;
            }
        }
        if (recursiveCount < 1) return;
        if (recursiveCount > 1) {
            // TODO
            throw new ConvertException(recursiveRule.location, "unimplemented: several recursive rules");
        }
        if (!(recursiveRule instanceof SSequence)) {
            throw new ConvertException(recursiveRule.location, "unsupported recursion: should be sequence");
        }

        boolean leftRecursion;
        boolean isOptionalTail;
        List<SExpression> rulesyms = ((SSequence) recursiveRule).elements;
        if (rulesyms.last() instanceof SReference && ExpressionUtil.refers(rulesyms.last(), term)) {
            leftRecursion = false;
            isOptionalTail = ((SReference) rulesyms.last()).isOptional;
            rulesyms = rulesyms[0..-2];
        } else if (rulesyms.first() instanceof SReference && ExpressionUtil.refers(rulesyms.first(), term)) {
            leftRecursion = true;
            isOptionalTail = ((SReference) rulesyms.first()).isOptional;
            rulesyms = rulesyms[1..-1];
        } else {
            throw new ConvertException(recursiveRule.location, "unsupported recursion: should be left or right");
        }
        if (rulesyms.any {ExpressionUtil.refers(it, term)} || rulesyms.isEmpty()) {
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
                if (ExpressionUtil.isEmpty(rule1)) {
                    term.value = quantifier;
                } else if (!isOptionalTail && ExpressionUtil.equals(rule1, ((SQuantifier) quantifier).inner)) {
                    ((SQuantifier) quantifier).min = 1;
                    term.value = quantifier;
                } else {
                    term.value = SUtil.createSequence(leftRecursion ? [rule1, quantifier] : [quantifier, rule1], term.location);
                }
            } else {
                if (tt.any {ExpressionUtil.isEmpty(it)}) {
                    tt.removeAll {ExpressionUtil.isEmpty(it)};
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
        while (!queue.isEmpty()) {
            SSymbol first = queue.remove();
            assert first.isEntry;
            for (SSymbol s: ExpressionUtil.getReferencedSymbols(first.value)) {
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
