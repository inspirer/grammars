package org.textway.tools.converter.handlers.part

import org.textway.tools.converter.handlers.LinePartHandler
import org.textway.tools.converter.handlers.ReaderOptions
import org.textway.tools.converter.spec.SLookahead
import org.textway.tools.converter.spec.SUtil

class LookaheadPartDetector implements LinePartHandler {

    String handleParts(String line, String location, ReaderOptions opts) {
        return line.replaceAll(/\[lookahead\s*[!=]=\s*[^\]]+\]/) {
            def matcher = it =~ /\[lookahead\s*([!=])=\s*([^\]]+)\]/;
            boolean inverted = matcher[0][1] == '!';
            List<String> symbols = matcher[0][2].toString().trim().split(/\s+/)
            opts.registry.nextId(new SLookahead(symbols.collect { SUtil.create(it, location, opts) }, !inverted))
        }
    }
}
