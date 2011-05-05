package org.textway.tools.converter.parser

class ParseException extends Exception {
    def location

    ParseException(def location, String message) {
        super(message)
        this.location = location
    }

    @Override
    String toString() {
        return "${location}: " + getMessage()
    }
}
