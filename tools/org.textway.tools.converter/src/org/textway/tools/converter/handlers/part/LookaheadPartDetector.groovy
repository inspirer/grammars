package org.textway.tools.converter.handlers.part

import org.textway.tools.converter.handlers.LinePartHandler
import org.textway.tools.converter.handlers.ReaderOptions

class LookaheadPartDetector implements LinePartHandler {

    String handleParts(String line, String location, ReaderOptions opts) {
        return line.replaceAll(/\[lookahead\s*!=\s*[^\]]+\]/) {
            // TODO replace through registry
            println "found: " + it;
            ""
        }
    }
}
