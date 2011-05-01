package org.textway.tools.converter.handlers

class PartsRegistry {
    def partid2expr = [:]
    int counter = 1;

    String nextId() {
        return "_____partid" + counter++;
    }
}
