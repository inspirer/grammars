package org.textway.tools.converter.parser.part

import org.textway.tools.converter.parser.LinePartHandler
import org.textway.tools.converter.parser.ReaderOptions

import org.textway.tools.converter.spec.SUtil
import org.textway.tools.converter.parser.SReaderUtil

/*
 *   Samples:
 *      [lookahead != { function] Expression ;
 *      <CR> [lookahead != <LF>]
 */
class LookaheadPartDetector implements LinePartHandler {

    String handleParts(String line, String location, ReaderOptions opts) {
        return line.replaceAll(/\[lookahead\s*[!=]=\s*[^\]]+\]/) {
            def matcher = it =~ /\[lookahead\s*([!=])=\s*([^\]]+)\]/;
            boolean inverted = matcher[0][1] == '!';
            List<String> symbols = matcher[0][2].toString().trim().split(/\s+/)
            opts.registry.nextId(SUtil.createLookahead(symbols.collect { SReaderUtil.create(it, location, opts) }, inverted, location))
        }
    }
}
