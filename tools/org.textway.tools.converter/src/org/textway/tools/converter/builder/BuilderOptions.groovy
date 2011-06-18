package org.textway.tools.converter.builder

import org.textway.tools.converter.ConvertException

/**
 *  evgeny, 6/18/11
 */
class BuilderOptions {

    def options = [
            'delimeters': [],
            'nonTerm': [],
            'input': [],
    ];

    BuilderOptions() {
    }

    private boolean acceptLine(String key, String value, String location) {
        if (key == 'delimeters' || key == 'nonTerm' || key == 'input') {
            def settings = [];
            value.split(/,/).each { settings.add(it.trim()) }
            options[key] = settings;
            return true;
        }
        return false;
    }

    BuilderOptions read(File file) {
        int linenum = 0;
        file.eachLine {
            line ->
            linenum++;
            def location = "${file.name},${linenum}";
            if (line =~ /^#/ || line.trim().isEmpty()) return;

            def matcher;
            if ((matcher = line =~ /^(\w+)\s+(.*?)\s*$/)) {
                if (!acceptLine((String) matcher[0][1], (String) matcher[0][2], location)) {
                    throw new ConvertException(location, "cannot parse\n${line}");
                }
                return;
            }

            if (line.trim().length() > 0) {
                throw new ConvertException(location, "bad line\n${line}");
            }
        };
        return this;
    }
}
