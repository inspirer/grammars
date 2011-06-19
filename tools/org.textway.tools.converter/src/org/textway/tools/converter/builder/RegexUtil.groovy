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
            return "\\u" + ss;
        }
        return c;
    }

    static void handleRegexp(SExpression expr, StringBuilder sb) {
        sb.append("/");
        buildRegexp(expr, sb, 0, false);
        sb.append("/");
    }

    private static boolean toCharacterSet(SExpression expr, List<SExpression> plus) {
        expr = ExpressionUtil.unwrap(expr);
        if (expr instanceof SReference) {
            return toCharacterSet(expr.resolved.value, plus);
        } else if (expr instanceof SChoice) {
            return expr.elements.every { toCharacterSet(it, plus) }
        } else if (expr instanceof SCharacter || expr instanceof SUnicodeCategory) {
            plus.add(expr);
            return true;
        }
        return false;
    }

    private static void buildRegexp(SExpression expr, StringBuilder sb, int deep, boolean needParens) {
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
                buildRegexp(expr.elements.first(), sb, deep, needParens);
            } else {
                def plus = [];
                if (toCharacterSet(expr, plus)) {
                    sb.append(setAsString(plus, []));
                } else {
                    if (needParens) sb.append("(");
                    boolean first = true;
                    for (SExpression e: expr.elements) {
                        if (!first) {
                            sb.append("|");
                        } else {
                            first = false;
                        }
                        buildRegexp(e, sb, deep, !(e instanceof SSequence));
                    }
                    if (needParens) sb.append(")");
                }
            }
        } else if (expr instanceof SSequence) {
            if (needParens) sb.append("(");
            for (SExpression e: expr.elements) {
                buildRegexp(e, sb, deep, true);
            }
            if (needParens) sb.append(")");
        } else if (expr instanceof SCharacter) {
            sb.append(toRegexp(((SCharacter) expr).c, false))

        } else if (expr instanceof SUnicodeCategory) {
            sb.append("\\p{" + expr.name + "}")

        } else if (expr instanceof SReference) {
            def sym = expr.resolved;
            buildRegexp(sym.value, sb, deep + 1, needParens || expr.isOptional);
            if (expr.isOptional) {
                sb.append('?');
            }
        } else if (expr instanceof SQuantifier) {
            buildRegexp(expr.inner, sb, deep + 1, true);
            sb.append(quantifierAsString(expr.min, expr.max));
        } else if (expr instanceof SRegexp) {
            sb.append(expr.text[1..-2]);

        } else if( expr instanceof SSetDiff) {
            if(isCharacterSet(((SSetDiff)expr).left) && isCharacterSet(((SSetDiff)expr).right)) {
                def left = ((SSetDiff)expr).left instanceof SChoice ? ((SChoice)((SSetDiff)expr).left).elements : [((SSetDiff)expr).left];
                def right = ((SSetDiff)expr).right instanceof SChoice ? ((SChoice)((SSetDiff)expr).right).elements : [((SSetDiff)expr).right];
                sb.append(setAsString(left, right));
                return;
            }
            sb.append("<unknown ${expr} >")

        } else if(expr instanceof SLookahead) {
            sb.append(expr.inverted ? "(?!" : "(?=");
            def plus = [];
            if(expr.terms.every {toCharacterSet(it, plus)}) {
                sb.append(setAsString(plus, []));
            } else {
                buildRegexp(SUtil.createChoice(expr.terms, expr.location), sb, deep, true);
            }
            sb.append(")")
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

    private static void setElementToString(StringBuilder sb, List<SExpression> list) {
        List<Integer> charList = list.findAll {it instanceof SCharacter}.collect { (int) ((SCharacter) it).c};
        Integer[] chars = charList.toArray(new Integer[charList.size()]);
        Arrays.sort(chars);
        for (int i = 0; i < chars.length; i++) {
            sb.append(toRegexp((char) chars[i], true));
            if (i + 1 < chars.length && chars[i + 1] == chars[i] + 1) {
                int originalI = i;
                while (i + 1 < chars.length && chars[i + 1] == chars[i] + 1) {
                    i++;
                }
                if (originalI + 1 < i)
                    sb.append("-");
                sb.append(toRegexp((char) chars[i], true));
            }
        }

        for (SExpression expr: list.findAll { !(it instanceof SCharacter) }) {
            if (expr instanceof SUnicodeCategory) {
                sb.append("\\p{" + expr.name + "}");
            } else {
                throw new ConvertException(expr.location, "expr cannot be used in set");
            }
        }
    }

    static boolean isCharacterSet(SExpression e) {
        if(e instanceof SChoice) {
            return e.elements.every { isCharacterSet(it) };

        } else if(e instanceof SCharacter || e instanceof SUnicodeCategory) {
            return true;
        }

        return false;
    }

    static String setAsString(List<SExpression> plus, List<SExpression> minus) {
        StringBuilder sb = new StringBuilder();
        if(plus.size() == 1 && minus.isEmpty()) {
            def sym = plus.first();
            if(sym instanceof SCharacter) {
                return toRegexp(((SCharacter)sym).c, false);
            } else if(sym instanceof SUnicodeCategory) {
                return "\\p{" + ((SUnicodeCategory)sym).name + "}";
            }
        }
        if (!plus.isEmpty()) {
            sb.append("[")
            setElementToString(sb, plus);
            sb.append("]")
        }
        if (!minus.isEmpty()) {
            if (plus.isEmpty()) {
                sb.append("[^")
            } else {
                sb.append("{-}[")
            }
            setElementToString(sb, minus);
            sb.append("]")
        }
        sb.toString()
    }
}
