package org.textway.tools.converter.handlers

import org.textway.tools.converter.ParseException

class ReaderOptions {
    PartsRegistry registry = new PartsRegistry();
    def options = [
            'lexem': false,
            'populate': [],
            'style': [],
    ];

    void acceptLine(String line, String location) {
        def matcher = null;
        if ((matcher = line =~ /^(style|populate)\s*=\s*([\w\-]+(\s*,\s*[\w\-]+)*)$/)) {
            String key = matcher[0][1];
            String s = matcher[0][2];
            def settings = [];
            s.split(/,/).each { settings.add(it.trim()) }
            options[key] = settings;

        } else if ((matcher = line =~ /^(lexem)\s*=\s*(true|false)$/)) {
            options['lexem'] = matcher[0][2] == 'true';
        } else {
            throw new ParseException(location, "cannot handle settings\n\t${line}");
        }
    }
}
