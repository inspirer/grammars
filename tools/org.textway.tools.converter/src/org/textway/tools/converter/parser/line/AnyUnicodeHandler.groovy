package org.textway.tools.converter.parser.line

import org.textway.tools.converter.parser.LineHandler
import org.textway.tools.converter.parser.ReaderOptions
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUtil

class AnyUnicodeHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        if (line =~ /^any Unicode code unit$/) {
            ((SChoice)sym.value).elements.add(SUtil.createAnyChar(location));
            return true
        }
        return false
    }
}
