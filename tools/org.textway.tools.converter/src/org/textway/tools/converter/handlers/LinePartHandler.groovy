package org.textway.tools.converter.handlers

interface LinePartHandler {

    String handleParts(String line, String location, ReaderOptions opts);
}
