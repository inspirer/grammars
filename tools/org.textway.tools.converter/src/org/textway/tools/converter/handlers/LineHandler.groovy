package org.textway.tools.converter.handlers

import org.textway.tools.converter.spec.SSymbol

public interface LineHandler {
    boolean tryHandle(SSymbol sym, String line, String location)
}