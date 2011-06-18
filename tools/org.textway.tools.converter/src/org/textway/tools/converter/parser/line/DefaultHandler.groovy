package org.textway.tools.converter.parser.line

import org.textway.tools.converter.parser.LineHandler
import org.textway.tools.converter.parser.ReaderOptions
import org.textway.tools.converter.parser.SReaderUtil
import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUtil

class DefaultHandler implements LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        def refs = SReaderUtil.create(line.split(/\s+/), location, opts)
        ((SChoice) sym.value).elements.add(refs.size() > 1 ? SUtil.createSequence(refs, location) : refs.first())
        return true
    }
}
