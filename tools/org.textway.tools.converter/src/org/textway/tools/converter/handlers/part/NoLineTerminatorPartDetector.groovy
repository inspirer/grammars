package org.textway.tools.converter.handlers.part

import org.textway.tools.converter.handlers.LinePartHandler
import org.textway.tools.converter.handlers.ReaderOptions
import org.textway.tools.converter.spec.SNoNewLine

class NoLineTerminatorPartDetector implements LinePartHandler {
    String handleParts(String line, String location, ReaderOptions opts) {
        return line.replaceAll(/\[no\s*LineTerminator\s*here\]/) {
            opts.registry.nextId(new SNoNewLine());
        }
    }
}
