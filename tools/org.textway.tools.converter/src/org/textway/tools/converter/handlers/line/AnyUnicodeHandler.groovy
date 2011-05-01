package org.textway.tools.converter.handlers.line

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SCharSet
import org.textway.tools.converter.handlers.LineHandler
import org.textway.tools.converter.handlers.ReaderOptions

class AnyUnicodeHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        if (line =~ /^any Unicode code unit$/) {
            ((SChoice)sym.value).elements.add(new SCharSet(1..65535));
            return true
        }
        return false
    }
}
