package org.spath.impl;

import java.math.BigDecimal;

import org.spath.SpathEvaluator;
import org.spath.SpathNameElement;
import org.spath.SpathNameRelative;
import org.spath.SpathNameStar;
import org.spath.SpathPredicateBoolean;
import org.spath.SpathPredicateNumber;
import org.spath.SpathPredicateString;

public class SpathElementEvaluator implements SpathEvaluator<SpathElement> {
    
    public SpathElementEvaluator() {
    }
    
    @Override
    public boolean match(SpathNameElement target, SpathElement event) {
        String targetValue = target.getName();
        String eventValue = event.getName();
        return targetValue.equals(eventValue);
    }
    
    @Override
    public boolean match(SpathNameRelative target, SpathElement event) {
        String targetValue = target.getName();
        String eventValue = event.getName();
        return targetValue.equals(eventValue);
    }

    @Override
    public boolean match(SpathNameStar target, SpathElement event) {
        return true;
    }
    
    @Override
    public boolean match(SpathPredicateString predicate, SpathElement event) {
        for (SpathProperty prop : event.getProperties()) {
            String name = prop.getName();
            String value = prop.getValueAsString();
            if (predicate.compareTo(name, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathPredicateNumber predicate, SpathElement event) {
        for (SpathProperty prop : event.getProperties()) {
            String name = prop.getName();
            BigDecimal value = prop.getValueAsNumber();
            if (predicate.compareTo(name, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathPredicateBoolean predicate, SpathElement event) {
        for (SpathProperty prop : event.getProperties()) {
            String name = prop.getName();
            Boolean value = prop.getValueAsBoolean();
            if (predicate.compareTo(name, value)) {
                return true;
            }
        }
        return false;
    }
}
