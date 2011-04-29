package org.textway.tools.converter.handlers

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SCharSet

class AnyUnicodeHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location) {
        if (line =~ /^any Unicode code unit$/) {
            ((SChoice)sym.value).elements.add(new SCharSet(1..65525));
            return true
        }
        return false
    }
}
