package org.textway.tools.converter.writer

import org.textway.tools.converter.spec.*

class SWriter {

    SLanguage language;
    StringBuilder sb;

    SWriter(SLanguage language, int level) {
        this.language = language
        sb = new StringBuilder();
    }

    String write() {
        sb.append("# ${language.name}\n\n");
        for (s in language.all) {
            write(s);
        }
        sb.toString();
    }

    void write(SSymbol s) {
        sb.append(s.name);

        boolean isOneOf = false;
        if (s.isTerm) {
            isOneOf = !s.isEntry && s.value instanceof SChoice &&
                    ((SChoice) s.value).elements.size() > 6 &&
                    (((SChoice) s.value).elements.any {it instanceof SCharacter} ||
                            ((SChoice) s.value).elements.any {it instanceof SReference});
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
        if (e instanceof SSequence) {
            if (e.elements == null || e.elements.isEmpty()) {
                return "[empty]";
            }
            return e.elements.collect { asString(it, true) }.join(" ");
        } else if (e instanceof SChoice) {
            return (needParens ? "(" : "") + e.elements.collect { asString(it, false) }.join(" or ") + (needParens ? ")" : "");
        } else if (e instanceof SCharacter) {
            return charmap.containsKey(e.c) ? "<" + charmap[e.c] + ">" : e.c;
        } else if (e instanceof SAnyChar) {
            return "[anychar]";
        } else if (e instanceof SLookahead) {
            return "[lookahead " + (e.inverted ? "!= " : "== ") + (e.terms.collect {asString(it, true)}.join(" ")) + "]";
        } else if (e instanceof SUnicodeCategory) {
            return "<" + e.name + ">";
        } else if (e instanceof SSetDiff) {
            return (needParens ? "[" : "") + asString(e.left, false) + " but not " + asString(e.right, false) + (needParens ? "]" : "");
        } else if (e instanceof SNoNewLine) {
            return "[no LineTerminator here]";
        } else if (e instanceof SReference) {
            return e.resolved.name + (e.isOptional ? "opt" : "");
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

}
