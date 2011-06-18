package org.textway.tools.converter.parser.line

import org.textway.tools.converter.parser.LineHandler
import org.textway.tools.converter.parser.ReaderOptions
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSequence
import org.textway.tools.converter.spec.SSymbol

class EmptyHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        if (line =~ /^\[empty\]$/) {
            ((SChoice) sym.value).elements.add(new SSequence());
            return true;
        }
        return false
    }
}
