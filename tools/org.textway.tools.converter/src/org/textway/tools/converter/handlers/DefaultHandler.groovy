package org.textway.tools.converter.handlers

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SReference
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSequence

class DefaultHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location) {
        def refs = SReference.create(line.split(/\s+/), location)
        ((SChoice) sym.value).elements.add(new SSequence(refs))
        return true
    }
}
