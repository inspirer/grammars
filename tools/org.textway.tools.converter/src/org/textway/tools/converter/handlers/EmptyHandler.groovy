package org.textway.tools.converter.handlers

import org.textway.tools.converter.spec.SSequence
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SChoice

class EmptyHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location) {
        if (line =~ /^\[empty\]$/) {
            ((SChoice)sym.value).elements.add(new SSequence());
            return true;
        }
        return false
    }
}
