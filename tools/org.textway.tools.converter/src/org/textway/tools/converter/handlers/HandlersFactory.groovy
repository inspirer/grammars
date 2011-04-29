package org.textway.tools.converter.handlers

class HandlersFactory {

    static def handlers = [
            'butnot': new ButNotHandler(),
            'emptyinbrackets': new EmptyHandler(),
            'anyunicode': new AnyUnicodeHandler(),
    ]

    static def defaultHandler = new DefaultHandler()

    static LineHandler getFor(def name) {
        handlers[name]
    }

    static LineHandler getDefault() {
        defaultHandler
    }
}
