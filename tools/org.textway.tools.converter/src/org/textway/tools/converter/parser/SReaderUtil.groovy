package org.textway.tools.converter.parser

import org.textway.tools.converter.spec.SExpression

class SReaderUtil {

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

    static List<SExpression> create(String[] s, String location, ReaderOptions opts) {
        return s.collect { create(it, location, opts) }
    }

    static SExpression create(String text, String location, ReaderOptions opts) {
        def matcher = null;
        if(opts.registry.partid2expr[text]) {
            return opts.registry.partid2expr[text];
        } else if ((matcher = text =~ /^<(\w{2,5})>$/)) {
            String cname = matcher[0][1];
            if(charmap[cname] != null) {
                return org.textway.tools.converter.spec.SUtil.createChar(charmap[cname], location);
            }
            if(cname.equals("USP")) {
                return org.textway.tools.converter.spec.SUtil.createUnicodeCategory("Zs", location);
            }
            throw new ParseException(location, "unknown character id: $text");
        } else if (opts.options['lexem'] && text.length() == 1) {
            return org.textway.tools.converter.spec.SUtil.createChar(text.charAt(0), location);
        } else {
            return org.textway.tools.converter.spec.SUtil.createReference(text, location);
        }
    }
}
