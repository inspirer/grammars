package org.textway.tools.converter.parser

interface LinePartHandler {

    String handleParts(String line, String location, ReaderOptions opts)
}
