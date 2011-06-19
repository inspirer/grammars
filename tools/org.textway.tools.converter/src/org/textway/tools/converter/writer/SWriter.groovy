package org.textway.tools.converter.writer

import org.textway.tools.converter.builder.ExpressionUtil
import org.textway.tools.converter.builder.RegexUtil
import org.textway.tools.converter.syntax.SRegexp
import org.textway.tools.converter.spec.*

class SWriter {

    SLanguage language;
    StringBuilder sb;
    int level
    def names = [:];

    SWriter(SLanguage language, int level) {
        this.language = language
        this.level = level
        sb = new StringBuilder();
    }

    String write() {

        sb.append("# ${language.name}\n\n");
        if (level >= 7) {
            for(SSymbol s : language.all.findAll { it.isEntry }) {
                names[s.name] = isIdentifier(s.name) ? s.name : '\'' + s.name + '\'';
            }

            for (s in language.all.findAll {it.isTerm && it.isEntry}) {
                writeLexem(s);
            }

            sb.append("\n# parser\n\n");

            for (s in language.all.findAll {!it.isTerm && it.isEntry}) {
                writeNonTerm(s);
            }

        } else {
            language.all.each {names[it.name] = it.name;}
            for (s in language.all) {
                write(s);
            }
        }

        sb.toString();
    }

    void writeLexem(SSymbol s) {
        if (s.value instanceof SChoice) {
            for (def t: ((SChoice) s.value).elements) {
                sb.append(names[s.name]);
                sb.append(": ");
                sb.append(((SRegexp) t).text);
                sb.append('\n');
            }
            sb.append('\n');
        } else {
            sb.append(names[s.name]);
            sb.append(": ");
            sb.append(((SRegexp) s.value).text);
            sb.append('\n');
        }
    }

    void writeNonTerm(SSymbol s) {
        sb.append(names[s.name]);
        sb.append(" ::=\n");
        if (s.value instanceof SChoice) {
            def elements = ((SChoice) s.value).elements;
            boolean first = true;
            for (e in elements) {
                sb.append('\t');
                sb.append(first ? '  ' : '| ');
                sb.append(asString(e, false))
                sb.append("\n");
                first = false;
            }
            sb.append(";\n")
        } else {
            sb.append('\t');
            sb.append(asString(s.value, false))
            sb.append(" ;\n");
        }
        sb.append('\n');
    }

    void write(SSymbol s) {
        sb.append(s.name);

        boolean isOneOf = false;
        if (s.isTerm) {
            isOneOf = !s.isEntry && s.value instanceof SChoice &&
                    ((SChoice) s.value).elements.size() > 6 &&
                    (((SChoice) s.value).elements.every {it instanceof SReference || it instanceof SCharacter});
            if (isOneOf) {
                sb.append(" :: one of\n")
            } else {
                sb.append(s.isEntry ? " :: (lexem)\n" : " ::\n")
            }
        } else {
            sb.append(" :\n");
        }

        if (isOneOf) {
            def elements = ((SChoice) s.value).elements;
            int i = 0, inarow = ((SChoice) s.value).elements.first() instanceof SCharacter ? 6 : 4;
            for (e in elements) {
                if ((i++ % inarow) == 0 && i > 1) {
                    sb.append("\n\t");
                } else {
                    sb.append("\t");
                }
                sb.append(asString(e, false))
            }
            sb.append("\n");

        } else if (s.value instanceof SChoice) {
            def elements = ((SChoice) s.value).elements;
            for (e in elements) {
                sb.append('\t');
                sb.append(asString(e, false))
                sb.append("\n");
            }
        } else {
            sb.append('\t');
            sb.append(asString(s.value, false))
            sb.append("\n");
        }
        sb.append('\n');
    }

    String asString(SExpression e, boolean needParens) {
        e = ExpressionUtil.unwrap(e);
        if (e instanceof SSequence) {
            if (e.elements == null || e.elements.isEmpty()) {
                return "[empty]";
            }
            if (e.elements.size() < 2) needParens = false;
            return (needParens ? "(" : "") + e.elements.collect { asString(it, true) }.join(" ") + (needParens ? ")" : "");
        } else if (e instanceof SChoice) {
            if (e.elements == null || e.elements.isEmpty()) {
                return "[empty]";
            }
            return (needParens ? "(" : "") + e.elements.collect { asString(it, !(it instanceof SSequence)) }.join(" or ") + (needParens ? ")" : "");
        } else if (e instanceof SCharacter) {
            return charmap.containsKey(e.c) ? "<" + charmap[e.c] + ">" : e.c;
        } else if (e instanceof SAnyChar) {
            return "[anychar]";
        } else if (e instanceof SLookahead) {
            return "[lookahead " + (e.inverted ? "!= " : "== ") + (e.terms.collect {asString(it, true)}.join(" ")) + "]";
        } else if (e instanceof SUnicodeCategory) {
            return "<" + e.name + ">";
        } else if (e instanceof SSetDiff) {
            return (needParens ? "(" : "") + asString(e.left, false) + " but not " + asString(e.right, true) + (needParens ? ")" : "");
        } else if (e instanceof SNoNewLine) {
            return "[no LineTerminator here]";
        } else if (e instanceof SReference) {
            return names[e.resolved.name] + (e.isOptional ? "opt" : "");
        } else if (e instanceof SRegexp) {
            return ((SRegexp) e).text
        } else if (e instanceof SQuantifier) {
            return asString(e.inner, true) + RegexUtil.quantifierAsString(e.min, e.max)
        }
        return "[????????]";
    }

    static Map<Character, String> charmap = new HashMap<Character, String>();

    static {
        charmap.put((Character) 0x200C, 'ZWNJ');
        charmap.put((Character) 0x200D, 'ZWJ');
        charmap.put((Character) '\r', 'CR');
        charmap.put((Character) '\n', 'LF');
        charmap.put((Character) 0x000B, 'VT');
        charmap.put((Character) 0x000C, 'FF');
        charmap.put((Character) 0x2028, 'LS');
        charmap.put((Character) 0x2029, 'PS');
        charmap.put((Character) 0x00A0, 'NBSP');
        charmap.put((Character) '\t', 'TAB');
        charmap.put((Character) 0x0020, 'SP');
        charmap.put((Character) 0xFEFF, 'BOM');
    }

    boolean isIdentifier(String s) {
        return s.matches(~/[a-zA-Z_][a-zA-Z_0-9]*/);
    }
}
