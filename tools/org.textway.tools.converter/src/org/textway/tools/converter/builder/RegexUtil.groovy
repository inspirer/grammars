package org.textway.tools.converter.builder

import org.textway.tools.converter.ConvertException
import org.textway.tools.converter.syntax.SRegexp
import org.textway.tools.converter.spec.*

class RegexUtil {

    static String toRegexp(char c, boolean inSet) {
        switch (c) {
            case '\r': return "\\r";
            case '\n': return "\\n";
            case '\t': return "\\t";

            case '(': case ')': case '[': case ']': case '|': case '{': case '}':
            case '*': case '+': case '?':
            case '\\': case '/':
            case ".":
                return "\\${c}";
        }
        if (inSet && c == "-") {
            return "\\${c}";
        }
        if (c < 0x20 || c >= 0x80) {
            def ss = Integer.toHexString((int) c);
            ss = "0000".substring(ss.length()) + ss
            return "\\x" + ss;
        }
        return c;
    }

    static void handleRegexp(SExpression expr, StringBuilder sb) {
        sb.append("/");
        buildRegexp(expr, sb, 0);
        sb.append("/");
    }

    private static void buildRegexp(SExpression expr, StringBuilder sb, int deep) {
        if (expr == null) {
            throw new ConvertException("unknown", "null expression");
        }
        if (deep > 128) {
            throw new ConvertException(expr.location, "lexems: cycle detected");
        }

        if (expr instanceof SChoice) {
            int size = expr.elements.size();
            if (size == 0) {
                throw new ConvertException(expr.location, "empty choice");
            } else if (size == 1) {
                buildRegexp(expr.elements.first(), sb, deep);
            } else {
                sb.append("(");
                boolean first = true;
                for (SExpression e: expr.elements) {
                    if (!first) {
                        sb.append("|");
                    } else {
                        first = false;
                    }
                    buildRegexp(e, sb, deep);
                }
                sb.append(")");
            }
        } else if (expr instanceof SSequence) {
            for (SExpression e: expr.elements) {
                buildRegexp(e, sb, deep);
            }
        } else if (expr instanceof SCharacter) {
            sb.append(toRegexp(((SCharacter) expr).c, false))

        } else if (expr instanceof SUnicodeCategory) {
            sb.append("\\p{" + expr.name + "}")

//        } else if(expr instanceof SReference) {
            //            def sym = expr.resolved;
            //            buildRegexp(sym.value, sb, deep+1);

        } else {
            sb.append("<unknown ${expr} >")
        }
    }

    static SRegexp create(String regexp, String location) {
        SRegexp re = new SRegexp()
        re.text = regexp
        re.location = location
        return re
    }

    static String quantifierAsString(int min, int max) {
        if (max == -1) {
            if (min == 0) {
                return '*';
            } else if (min == 1) {
                return '+';
            } else {
                return "{${min},}";
            }
        } else if (min == 0 && max == 1) {
            return '?';
        } else if (min == max) {
            return "{${min}}";
        } else {
            return "{${min},${max}}";
        }
    }

    private static void setElementToString(StringBuilder sb, SExpression expr) {
        if (expr instanceof SCharacter) {
            sb.append(toRegexp(expr.c, true));
        } else if (expr instanceof SUnicodeCategory) {
            sb.append("\\p{" + expr.name + "}");
        } else {
            throw new ConvertException(expr.location, "expr cannot be used in set");
        }
    }

    static boolean isCharacterSet(List<SExpression> list) {
        return list.every {it instanceof SCharacter || it instanceof SUnicodeCategory};
    }

    static String setAsString(List<SExpression> plus, List<SExpression> minus) {
        StringBuilder sb = new StringBuilder();
        if (!plus.isEmpty()) {
            sb.append("[")
            plus.each { setElementToString(sb, it) }
            sb.append("]")
        }
        if (!minus.isEmpty()) {
            if (plus.isEmpty()) {
                sb.append("[^")
            } else {
                sb.append("{-}[")
            }
            minus.each { setElementToString(sb, it) }
            sb.append("]")
        }
    }
}
