package org.spath.data;

import java.math.BigDecimal;

import org.spath.SpathEvaluator;
import org.spath.SpathNameElement;
import org.spath.SpathNameRelative;
import org.spath.SpathNameStar;
import org.spath.SpathPredicateBoolean;
import org.spath.SpathPredicateNumber;
import org.spath.SpathPredicateString;

public class SpathEventEvaluator implements SpathEvaluator<SpathEvent> {
    
    public SpathEventEvaluator() {
    }
    
    @Override
    public boolean match(SpathNameElement target, SpathEvent event) {
        String targetValue = target.getName();
        String eventValue = event.getName();
        return targetValue.equals(eventValue);
    }
    
    @Override
    public boolean match(SpathNameRelative target, SpathEvent event) {
        String targetValue = target.getName();
        String eventValue = event.getName();
        return targetValue.equals(eventValue);
    }

    @Override
    public boolean match(SpathNameStar target, SpathEvent event) {
        return true;
    }
    
    @Override
    public boolean match(SpathPredicateString predicate, SpathEvent event) {
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
    public boolean match(SpathPredicateNumber predicate, SpathEvent event) {
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
    public boolean match(SpathPredicateBoolean predicate, SpathEvent event) {
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
