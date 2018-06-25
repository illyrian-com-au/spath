package org.spath;

public enum SpathOperator {
    EQ("="),
    NE("!="),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">=");
    
    private final String value;
    
    private SpathOperator(String value) {
        this.value = value;
    }
    
    public boolean match(Object other) {
        return value == other.toString();
    }
    
    public String toString() {
        return value;
    }
}
