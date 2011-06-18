package org.textway.tools.converter.parser.line

import org.textway.tools.converter.parser.LineHandler
import org.textway.tools.converter.parser.ParseException
import org.textway.tools.converter.parser.ReaderOptions
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUnicodeCategory
import org.textway.tools.converter.spec.SUtil

/*
 *   Samples:
 *      any character in the Unicode categories "Uppercase letter (Lu)", "Lowercase letter (Ll)", "Titlecase letter (Lt)", "Modifier letter (Lm)", "Other letter (Lo)", or "Letter number (Nl)"
 *      any character in the Unicode category "Decimal number (Nd)"
 */
class UnicodeCategoriesHandler implements LineHandler {

    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        def matcher;
        if ((matcher = line =~ /^any character in the Unicode categor(ies|y)\s*(.*)$/)) {
            String tail = matcher[0][2]
            def foundclasses = []
            while ((matcher = tail =~ /^"([\-\w\s]+\((\w+)\))"/)) {
                String charclass = matcher[0][2];
                foundclasses.add(charclass);
                tail = tail.substring(matcher.end());
                if ((matcher = tail =~ /^\s*(,(\s*or)?|or)\s*/)) {
                    tail = tail.substring(matcher.end());
                }
            }
            if (!tail.isEmpty()) {
                throw new ParseException(location, "cannot parse unicode category line\n\ttail is `${tail}'");
            }

            List<SUnicodeCategory> charsets = foundclasses.collect { SUtil.createUnicodeCategory(it, location) }
            ((SChoice) sym.value).elements.add(SUtil.createChoice(charsets, location))
            return true;
        }
        return false
    }
}
