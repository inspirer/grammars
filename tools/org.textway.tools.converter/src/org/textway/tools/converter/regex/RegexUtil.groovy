package org.textway.tools.converter.regex

import org.textway.tools.converter.spec.SCharacter
import org.textway.tools.converter.spec.SSequence
import org.textway.tools.converter.spec.SExpression
import org.textway.tools.converter.parser.ParseException
import org.textway.tools.converter.spec.SChoice

class RegexUtil {

    String toRegexp(char c, boolean inSet) {
        switch(c) {
            case '\r': return "\\r";
            case '\n': return "\\n";
            case '\t': return "\\t";

            case '(': case ')': case '[': case ']': case '|': case '{': case '}':
            case '*': case '+': case '?':
            case '\\': case '/':
            case ".":
                return "\\${c}";
        }
        if(inSet && c == "-") {
            return "\\${c}";
        }
        if(c < 0x20 || c >= 0x80) {
            def ss = Integer.toHexString(c);
            ss = "0000".substring(ss.length()) + ss
            return "\\x" + ss;
        }
        return c;
    }

    private boolean handleRegexp(SExpression expr, StringBuilder sb) {
        if(expr == null) {
            throw new ParseException(expr.location, "null expression");
        }

        if(expr instanceof SChoice) {
            int size = expr.elements.size();
            if(size == 0) {
                throw new ParseException(expr.location, "empty choice");
            } else if(size == 1) {
                handleRegexp(expr.elements.first(), sb);
            } else {
                sb.append("(");
                boolean first = true;
                for(SExpression e : expr.elements) {
                    if(!first) {
                        sb.append("|");
                    } else {
                        first = false;
                    }
                    handleRegexp(e, sb);
                }
                sb.append(")");
            }
        } else if(expr instanceof SSequence) {
            for(SExpression e : expr.elements) {
                handleRegexp(e, sb);
            }
        } else if(expr instanceof SCharacter) {
            // TODO sb.append(((SCharacter)expr).asRegexp(false))

        } else {
            sb.append("<unknown ${expr} >")
        }
        return false;
    }

}
