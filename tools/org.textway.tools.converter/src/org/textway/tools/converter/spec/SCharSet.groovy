package org.textway.tools.converter.spec

class SCharSet implements SExpression {
    List<Range<Integer>> ranges;

    SCharSet(Range<Integer> ...ranges) {
        this.ranges = ranges.toList()
    }

    void accept(SVisitor visitor) {
    }
}
