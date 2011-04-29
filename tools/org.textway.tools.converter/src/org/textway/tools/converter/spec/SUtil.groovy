package org.textway.tools.converter.spec

import org.textway.tools.converter.ParseException

class SUtil {

    static Map<String,Character> charmap = [
            'ZWNJ': (Character) 0x200C,
            'ZWJ': (Character) 0x200D,
            'CR': (Character) '\r',
            'LF': (Character) '\n',
            'VT': (Character) 0x000B,
            'FF': (Character) 0x000C,
            'LS': (Character) 0x2028,
            'PS': (Character) 0x2029,
            'NBSP': (Character) 0x00A0,
            'TAB': (Character) '\t',
            'SP': (Character) 0x0020,
            'BOM': (Character) 0xFEFF,
    ]

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

    static SExpression create(String text, String location) {
        def matcher = null;
        if ((matcher = text =~ /^<(\w{2,5})>$/)) {
            String cname = matcher[0][1];
            if(charmap[cname] != null) {
                return new SCharacter(charmap[cname]);
            }
            if(cname.equals("USP")) {
                return getUnicodeCategory("Zs", location);
            }
            throw new ParseException(location, "unknown character id: $text");
        } else if (text.length() == 1) {
            return new SCharacter(text.charAt(0));
        } else {
            return new SReference(text, location);
        }
    }

    static Map<String,SCharSet> categorycache = [:]

    static SCharSet getUnicodeCategory(String name, String location) {
        if(categorycache.containsKey(name)){
            return categorycache[name];
        }
        if(!unicodecategory.containsKey(name)) {
            throw new ParseException(location, "unknown unicode category: ${name}");
        }

        byte val = unicodecategory[name];

        List<Range<Integer>> result = []
        int rangestart = -1;
        int rangelast = -1;
        for(int i = 0; i < 65536; i++) {
            if(Character.getType(i) == val) {
                if(rangestart >= 0 && rangelast < i -1) {
                    result.add(rangestart .. rangelast);
                    rangestart = -1;
                }

                if(rangestart == -1) {
                    rangestart = rangelast = i;
                } else {
                    rangelast = i;
                }
            }
        }
        if(rangestart >= 0) {
            result.add(rangestart .. rangelast);
        }
        categorycache[name] = new SCharSet(result)
        return categorycache[name];
    }
}
