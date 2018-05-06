package org.spath;

import java.math.BigDecimal;


public class SpathPredicateNumber implements SpathMatch {
    private final String name;
    private final SpathOperator operator;
    private final BigDecimal value;
    
    public SpathPredicateNumber(String name, SpathOperator operator, BigDecimal value) {
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
            } else if (getOperator() == SpathOperator.EQ) {
                return (0 == getValue().compareTo(number));
            } else if (getOperator() == SpathOperator.NE){
                return (0 != getValue().compareTo(number));
            } else if (getOperator() == SpathOperator.LT){
                return (0 < getValue().compareTo(number));
            } else if (getOperator() == SpathOperator.LE){
                return (0 <= getValue().compareTo(number));
            } else if (getOperator() == SpathOperator.GT){
                return (0 > getValue().compareTo(number));
            } else if (getOperator() == SpathOperator.GE){
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
