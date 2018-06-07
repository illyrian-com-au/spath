package org.spath;

public class SpathPredicateString implements SpathPredicate {
    private final String name;
    private final SpathOperator operator;
    private final String value;
    
    public SpathPredicateString(String name) {
        this(name, null, null);
    }

    public SpathPredicateString(String name, SpathOperator operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public SpathOperator getOperator() {
        return operator;
    }

    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }

    public boolean compareTo(String name, String value) {
        if (getName().equals(name)) {
            if (getOperator() == null) {
                return true;
            } else if (getOperator() == SpathOperator.EQ) {
                return (0 == getValue().compareTo(value));
            } else if (getOperator() == SpathOperator.NE){
                return (0 != getValue().compareTo(value));
            } else if (getOperator() == SpathOperator.LT){
                return (0 < getValue().compareTo(value));
            } else if (getOperator() == SpathOperator.LE){
                return (0 <= getValue().compareTo(value));
            } else if (getOperator() == SpathOperator.GT){
                return (0 > getValue().compareTo(value));
            } else if (getOperator() == SpathOperator.GE){
                return (0 >= getValue().compareTo(value));
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SpathPredicateString) {
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
            build.append('\'');
            build.append(value);
            build.append('\'');
        }
        return build.toString();
    }
}
