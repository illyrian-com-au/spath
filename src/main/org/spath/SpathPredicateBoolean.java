package org.spath;

public class SpathPredicateBoolean implements SpathMatch {
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private final String name;
    private final SpathOperator operator;
    private final Boolean value;
    
    public SpathPredicateBoolean(String name, SpathOperator operator, Boolean value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public SpathOperator getOperator() {
        return operator;
    }

    public Boolean getValue() {
        return value;
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }
    
    public Boolean getValueAsBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            return getValueAsBoolean((String)value);
        }
        return null;
    }
    
    public Boolean getValueAsBoolean(String valueString) {
        if (TRUE.equalsIgnoreCase(valueString)) {
            return Boolean.TRUE;
        } else if (FALSE.equalsIgnoreCase(valueString)) {
            return Boolean.FALSE;
        }
        return null;
    }

    public boolean compareTo(String name, Boolean value) {
        if (getName().equals(name) && value != null) {
            if (getOperator() == null) {
                return true;
            } else if (getOperator() == SpathOperator.EQ) {
                return (0 == getValue().compareTo(value));
            } else if (getOperator() == SpathOperator.NE){
                return (0 != getValue().compareTo(value));
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SpathPredicateBoolean) {
            return toString().equals(other.toString());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("@");
        build.append(name);
        if (operator != null) {
            build.append(operator);
            build.append(value);
        }
        return build.toString();
    }
}
