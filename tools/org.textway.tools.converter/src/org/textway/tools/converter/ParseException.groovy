package org.textway.tools.converter

class ParseException extends Exception {
    int line

    ParseException(int line, String message) {
        super(message)
        this.line = line
    }

    @Override
    String toString() {
        return "${line}: " + getMessage()
    }
}
