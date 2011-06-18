package org.textway.tools.converter

class ConvertException extends Exception {
    def location

    ConvertException(def location, String message) {
        super(message)
        this.location = location
    }

    @Override
    String toString() {
        return "${location}: " + getMessage()
    }
}
