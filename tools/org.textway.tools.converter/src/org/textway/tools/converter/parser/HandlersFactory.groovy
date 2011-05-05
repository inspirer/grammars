package org.textway.tools.converter.parser

import org.textway.tools.converter.parser.part.LookaheadPartDetector
import org.textway.tools.converter.parser.part.NoLineTerminatorPartDetector
import org.textway.tools.converter.parser.line.*

class HandlersFactory {

    static def handlers = [
            'butnot': new ButNotHandler(),
            'emptyinbrackets': new EmptyHandler(),
            'anyunicode': new AnyUnicodeHandler(),
            'unicodecategories': new UnicodeCategoriesHandler(),
            'lookahead': new LookaheadPartDetector(),
            'nonewline': new NoLineTerminatorPartDetector(),
    ]

    static def defaultHandler = new DefaultHandler()

    static def getHandler(def name) {
        handlers[name]
    }

    static LineHandler getDefault() {
        defaultHandler
    }
}
