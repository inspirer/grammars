package org.textway.tools.converter.spec

class SLanguage {
    String name
    List<SSymbol> all;
    List<SSymbol> inputs;
    List<SSymbol> lexems;

    SLanguage(List<SSymbol> all, List<SSymbol> inputs) {
        this.all = all
        this.inputs = inputs
    }

    void detectLexems() {
        Set<SSymbol> usedTerms = [];
        SVisitor v = new SVisitor() {
            void visit(SReference ref) {
                if(ref.resolved.isTerm) {
                    usedTerms += ref.resolved;
                }
            }
        }
        all.each {
            if(!it.isTerm) {
              it.value.accept v
            }
        }

        lexems = usedTerms.toList().sort { it.name }
        println lexems
    }
}
