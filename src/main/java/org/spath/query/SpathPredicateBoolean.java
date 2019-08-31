package org.spath.query;

import org.spath.SpathEvaluator;
import org.spath.SpathPredicate;

public class SpathPredicateBoolean implements SpathPredicate {
    private final String name;
    private final SpathPredicateOperator operator;
    private final Boolean value;
    
    public SpathPredicateBoolean(String name, SpathPredicateOperator operator, Boolean value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public SpathPredicateOperator getOperator() {
        return operator;
    }

    public Boolean getValue() {
        return value;
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }
    
    public boolean compareTo(String name, Boolean value) {
        if (getName().equals(name) && value != null) {
            if (getOperator() == null) {
                return true;
            } else if (getOperator() == SpathPredicateOperator.EQ) {
                return (0 == getValue().compareTo(value));
            } else if (getOperator() == SpathPredicateOperator.NE){
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
