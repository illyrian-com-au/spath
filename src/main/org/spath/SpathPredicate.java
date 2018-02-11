package org.spath;

public class SpathPredicate {
    private final String name;
    private final String operator;
    private final String value;
    
    public SpathPredicate(String name, String operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SpathPredicate) {
            return toString().equals(other.toString());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append('@');
        build.append(name);
        build.append(operator);
        build.append(value);
        return build.toString();
    }
}
