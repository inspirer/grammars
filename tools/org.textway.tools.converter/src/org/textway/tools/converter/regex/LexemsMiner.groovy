package org.textway.tools.converter.regex

import org.textway.tools.converter.spec.SLanguage
import org.textway.tools.converter.spec.SSymbol

class LexemsMiner {

    static void detectLexems(SLanguage lang) {
        Set<SSymbol> usedTerms = [];
//        SVisitor v = new SVisitor() {
//            void visit(SReference ref) {
//                if(ref.resolved.isTerm) {
//                    usedTerms += ref.resolved;
//                }
//            }
//        }
//        all.each {
//            if(!it.isTerm) {
//              it.value.accept v
//            }
//        }
//
//        lexems = usedTerms.toList().sort { it.name }
//        println lexems
    }

}
