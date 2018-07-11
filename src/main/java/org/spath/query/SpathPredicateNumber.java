package org.spath.query;

import java.math.BigDecimal;

import org.spath.SpathEvaluator;
import org.spath.SpathPredicate;


public class SpathPredicateNumber implements SpathPredicate {
    private final String name;
    private final SpathPredicateOperator operator;
    private final BigDecimal value;
    
    public SpathPredicateNumber(String name, SpathPredicateOperator operator, BigDecimal value) {
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

    public BigDecimal getValue() {
        return value;
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }
    
    public boolean compareTo(String name, BigDecimal number) {
        if (getName().equals(name) && number != null) {
            if (getOperator() == null) {
                return true;
            } else if (getOperator() == SpathPredicateOperator.EQ) {
                return (0 == getValue().compareTo(number));
            } else if (getOperator() == SpathPredicateOperator.NE){
                return (0 != getValue().compareTo(number));
            } else if (getOperator() == SpathPredicateOperator.LT){
                return (0 < getValue().compareTo(number));
            } else if (getOperator() == SpathPredicateOperator.LE){
                return (0 <= getValue().compareTo(number));
            } else if (getOperator() == SpathPredicateOperator.GT){
                return (0 > getValue().compareTo(number));
            } else if (getOperator() == SpathPredicateOperator.GE){
                return (0 >= getValue().compareTo(number));
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SpathPredicateNumber) {
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
