package org.textway.tools.converter.handlers.line

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SReference
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSequence
import org.textway.tools.converter.spec.SUtil
import org.textway.tools.converter.handlers.LineHandler
import org.textway.tools.converter.handlers.ReaderOptions

class DefaultHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        def refs = SUtil.create(line.split(/\s+/), location, opts)
        ((SChoice) sym.value).elements.add(refs.size() > 1 ? new SSequence(refs) : refs.first())
        return true
    }
}
