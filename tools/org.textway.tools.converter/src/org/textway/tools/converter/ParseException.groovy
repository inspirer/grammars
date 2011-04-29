package org.textway.tools.converter

class ParseException extends Exception {
    def line

    ParseException(def line, String message) {
        super(message)
        this.line = line
    }

    @Override
    String toString() {
        return "${line}: " + getMessage()
    }
}
