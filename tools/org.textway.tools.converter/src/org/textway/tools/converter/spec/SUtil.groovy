package org.textway.tools.converter.spec

import org.textway.tools.converter.ConvertException

class SUtil {

    static def unicodecategory = [
            "Lu": Character.UPPERCASE_LETTER,
            "Ll": Character.LOWERCASE_LETTER,
            "Lt": Character.TITLECASE_LETTER,
            "Lm": Character.MODIFIER_LETTER,
            "Lo": Character.OTHER_LETTER,
            "Mn": Character.NON_SPACING_MARK,
            "Me": Character.ENCLOSING_MARK,
            "Mc": Character.COMBINING_SPACING_MARK,
            "Nd": Character.DECIMAL_DIGIT_NUMBER,
            "Nl": Character.LETTER_NUMBER,
            "No": Character.OTHER_NUMBER,
            "Zs": Character.SPACE_SEPARATOR,
            "Zl": Character.LINE_SEPARATOR,
            "Zp": Character.PARAGRAPH_SEPARATOR,
            "Cc": Character.CONTROL,
            "Cf": Character.FORMAT,
            "Co": Character.PRIVATE_USE,
            "Cs": Character.SURROGATE,
            "Pd": Character.DASH_PUNCTUATION,
            "Ps": Character.START_PUNCTUATION,
            "Pe": Character.END_PUNCTUATION,
            "Pc": Character.CONNECTOR_PUNCTUATION,
            "Po": Character.OTHER_PUNCTUATION,
            "Sm": Character.MATH_SYMBOL,
            "Sc": Character.CURRENCY_SYMBOL,
            "Sk": Character.MODIFIER_SYMBOL,
            "So": Character.OTHER_SYMBOL,
            "Pi": Character.INITIAL_QUOTE_PUNCTUATION,
            "Pf": Character.FINAL_QUOTE_PUNCTUATION,
    ]

//    static Map<String,SCharSet> categorycache = [:]

//    static SCharSet getUnicodeCategory(String name, String location) {
//        if(categorycache.containsKey(name)){
//            return categorycache[name];
//        }
//        if(!unicodecategory.containsKey(name)) {
//            throw new ConvertException(location, "unknown unicode category: ${name}");
//        }
//
//        byte val = unicodecategory[name];
//
//        List<Range<Integer>> result = []
//        int rangestart = -1;
//        int rangelast = -1;
//        for(int i = 0; i < 65536; i++) {
//            if(Character.getType(i) == val) {
//                if(rangestart >= 0 && rangelast < i -1) {
//                    result.add(rangestart .. rangelast);
//                    rangestart = -1;
//                }
//
//                if(rangestart == -1) {
//                    rangestart = rangelast = i;
//                } else {
//                    rangelast = i;
//                }
//            }
//        }
//        if(rangestart >= 0) {
//            result.add(rangestart .. rangelast);
//        }
//        categorycache[name] = new SCharSet(result)
//        return categorycache[name];
//    }

    static SAnyChar createAnyChar(String location) {
        SAnyChar res = new SAnyChar();
        res.location = location;
        return res;
    }

    static SCharacter createChar(char c, String location) {
        SCharacter res = new SCharacter();
        res.c = c
        res.location = location
        return res;
    }

    static SSetDiff createDiff(SExpression left, SExpression right, String location) {
        SSetDiff res = new SSetDiff();
        res.location = location;
        res.left = left;
        res.right = right;
        return res;
    }

    static SReference createReference(String text, String location) {
        SReference res = new SReference();
        res.internalText = text;
        res.location = location;
        return res;
    }

    static SChoice createChoice(List list, String location) {
        SChoice res = new SChoice();
        res.elements = list;
        res.location = location;
        return res;
    }

    static SSequence createSequence(List list, String location) {
        SSequence res = new SSequence();
        res.elements = list;
        res.location = location;
        return res;
    }

    static SUnicodeCategory createUnicodeCategory(String name, String location) {
        if (!unicodecategory.containsKey(name)) {
            throw new ConvertException(location, "unknown unicode category: ${name}");
        }
        def res = new SUnicodeCategory();
        res.name = name;
        res.location = location;
        return res;
    }

    static SLookahead createLookahead(List terms, boolean inverted, String location) {
        def res = new SLookahead();
        res.terms = terms;
        res.location = location;
        res.inverted = inverted;
        return res;
    }

    static SExpression createNoNewLine(String location) {
        def res = new SNoNewLine();
        res.location = location;
        return res;
    }

    static SSymbol createSymbol(String name, String location) {
        if (name.endsWith("opt")) {
            throw new ConvertException(location, "symbol cannot end with opt : ${name}");
        }
        def res = new SSymbol();
        res.name = name;
        res.location = location;
        res.value = createChoice([], location);
        return res;
    }
}
