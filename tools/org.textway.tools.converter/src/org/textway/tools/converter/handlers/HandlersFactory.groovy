package org.textway.tools.converter.handlers

import org.textway.tools.converter.handlers.part.LookaheadPartDetector
import org.textway.tools.converter.handlers.part.NoLineTerminatorPartDetector
import org.textway.tools.converter.handlers.line.*

class HandlersFactory {

    static def handlers = [
            'butnot': new ButNotHandler(),
            'emptyinbrackets': new EmptyHandler(),
            'anyunicode': new AnyUnicodeHandler(),
            'unicodecategories': new UnicodeCategoriesHandler(),
            'lookahead': new LookaheadPartDetector(),
            'nonewline': new NoLineTerminatorPartDetector(),
    ]

    static def parthandlers = [
    ]

    static def defaultHandler = new DefaultHandler()

    static def getHandler(def name) {
        handlers[name]
    }

    static LineHandler getDefault() {
        defaultHandler
    }
}
