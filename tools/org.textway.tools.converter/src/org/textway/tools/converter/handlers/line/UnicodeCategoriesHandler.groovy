package org.textway.tools.converter.handlers.line

import org.textway.tools.converter.handlers.ParseException
import org.textway.tools.converter.handlers.LineHandler
import org.textway.tools.converter.handlers.ReaderOptions
import org.textway.tools.converter.spec.SCharSet
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUtil

class UnicodeCategoriesHandler implements LineHandler {

    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        def matcher;
        if ((matcher = line =~ /^any character in the Unicode categor(ies|y)\s*(.*)$/)) {
            String tail = matcher[0][2]
            def foundclasses = []
            while((matcher = tail =~ /^"([\-\w\s]+\((\w+)\))"/)) {
                String charclass = matcher[0][2];
                foundclasses.add(charclass);
                tail = tail.substring(matcher.end());
                if((matcher = tail =~ /^\s*(,(\s*or)?|or)\s*/)) {
                    tail = tail.substring(matcher.end());
                }
            }
            if(!tail.isEmpty()) {
                throw new ParseException(location, "cannot parse unicode category line\n\ttail is `${tail}'");
            }

            List<SCharSet> charsets = foundclasses.collect { SUtil.getUnicodeCategory(it, location) }
            ((SChoice)sym.value).elements.add(SUtil.mergeSets(charsets))
            return true;
        }
        return false
    }
}
