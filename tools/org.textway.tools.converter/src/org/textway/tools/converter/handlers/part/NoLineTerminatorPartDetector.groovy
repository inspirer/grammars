package org.textway.tools.converter.handlers.part

import org.textway.tools.converter.handlers.LinePartHandler
import org.textway.tools.converter.handlers.ReaderOptions

class NoLineTerminatorPartDetector implements LinePartHandler {
    String handleParts(String line, String location, ReaderOptions opts) {
        return line.replaceAll(/\[no\s*LineTerminator\s*here\]/) {
            // TODO replace through registry
            ""
        }
    }
}
