package org.textway.tools.converter.handlers.line

import org.textway.tools.converter.spec.SSequence
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.handlers.LineHandler
import org.textway.tools.converter.handlers.ReaderOptions

class EmptyHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        if (line =~ /^\[empty\]$/) {
            ((SChoice)sym.value).elements.add(new SSequence());
            return true;
        }
        return false
    }
}
