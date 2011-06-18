package org.textway.tools.converter.parser.part

import org.textway.tools.converter.parser.LinePartHandler
import org.textway.tools.converter.parser.ReaderOptions
import org.textway.tools.converter.spec.SUtil

/*
 *  Sample:
 *    [no LineTerminator here]
 */
class NoLineTerminatorPartDetector implements LinePartHandler {
    String handleParts(String line, String location, ReaderOptions opts) {
        return line.replaceAll(/\[no\s*LineTerminator\s*here\]/) {
            opts.registry.nextId(SUtil.createNoNewLine(location));
        }
    }
}
