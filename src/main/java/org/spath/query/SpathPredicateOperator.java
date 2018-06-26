package org.spath.query;

public enum SpathPredicateOperator {
    EQ("="),
    NE("!="),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">=");
    
    private final String value;
    
    private SpathPredicateOperator(String value) {
        this.value = value;
    }
    
    public boolean match(Object other) {
        return value == other.toString();
    }
    
    public String toString() {
        return value;
    }
}
