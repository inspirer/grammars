package org.textway.tools.converter.parser

class ReaderOptions {
    PartsRegistry registry = new PartsRegistry();
    def options = [
            'lexem': false,
            'expand': [],
            'style': [],
    ];

    void acceptLine(String key, String value, String location) {
        if(key == 'parser' || key == 'lexer' || key == 'expand') {
            def settings = [];
            value.split(/,/).each { settings.add(it.trim()) }
            if(key == 'parser' || key == 'lexer') {
                options['style'] = settings;
                options['lexem'] = key == 'lexer';
            } else {
                options[key] = settings;
            }
        } else {
            throw new ParseException(location, "cannot handle settings\n\t${key}");
        }
    }
}
