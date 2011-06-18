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
