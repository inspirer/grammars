package org.textway.tools.converter.handlers

import org.textway.tools.converter.spec.SChoice
import org.textway.tools.converter.spec.SSetDiff
import org.textway.tools.converter.spec.SSymbol
import org.textway.tools.converter.spec.SUtil

class ButNotHandler implements LineHandler {

    boolean tryHandle(SSymbol sym, String line, String location) {
        def matcher;
        if ((matcher = line =~ /^(\w+)\s+but\s+not\s+(\S+)((\s*or\s+\S+)*)\s*$/)) {
            String origin = matcher[0][1];
            String tail = matcher[0][3]
            ArrayList<String> set = [matcher[0][2]];
            if (tail != null) {
                while ((matcher = tail =~ /\s*or\s+(\S+)/)) {
                    tail = tail.substring(matcher.end());
                    set.add((String) matcher[0][1]);
                }
                assert tail.isEmpty();
            }
            ((SChoice)sym.value).elements.add(
                    new SSetDiff(
                            SUtil.create(origin, location),
                            new SChoice(set.collect { SUtil.create(it, location) })));
            return true;
        }
        return false
    }

}