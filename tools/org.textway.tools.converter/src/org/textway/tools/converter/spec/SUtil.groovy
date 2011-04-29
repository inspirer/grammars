package org.textway.tools.converter.spec

class SUtil {
    static SExpression create(String text, String location) {
        if(text.length() == 1) {
            return new SCharacter(text.charAt(0));
        } else {
            return new SReference(text, location);
        }
    }
}
