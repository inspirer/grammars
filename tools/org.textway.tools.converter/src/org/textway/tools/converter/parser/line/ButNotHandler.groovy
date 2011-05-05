package org.textway.tools.converter.parser.line

import org.textway.tools.converter.spec.SChoice

import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUtil
import org.textway.tools.converter.parser.LineHandler
import org.textway.tools.converter.parser.ReaderOptions
import org.textway.tools.converter.parser.SReaderUtil

/*
 *  Samples:
 *      SourceCharacter but not ' or \ or LineTerminator
 *      IdentifierName but not ReservedWord
 */
class ButNotHandler implements LineHandler {

    boolean tryHandle(SSymbol sym, String line, String location, ReaderOptions opts) {
        def matcher;
        if ((matcher = line =~ /^(\w+)\s+but\s+not\s+(\S+)((\s+or\s+\S+)*)\s*$/)) {
            String origin = matcher[0][1];
            String tail = matcher[0][3]
            ArrayList<String> set = [matcher[0][2]];
            if (tail != null) {
                while ((matcher = tail =~ /\s+or\s+(\S+)/)) {
                    tail = tail.substring(matcher.end());
                    set.add((String) matcher[0][1]);
                }
                assert tail.isEmpty();
            }
            ((SChoice)sym.value).elements.add(
                    SUtil.createDiff(
                            SReaderUtil.create(origin, location, opts),
                            SUtil.createChoice(set.collect { SReaderUtil.create(it, location, opts) }, location),
                            location));
            return true;
        }
        return false
    }
}