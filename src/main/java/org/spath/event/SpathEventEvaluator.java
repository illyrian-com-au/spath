package org.spath.event;

import java.math.BigDecimal;

import org.spath.SpathEvaluator;
import org.spath.engine.SpathUtil;
import org.spath.query.SpathFunction;
import org.spath.query.SpathName;
import org.spath.query.SpathPredicateBoolean;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryException;

public class SpathEventEvaluator implements SpathEvaluator<SpathEvent> {
    
    public SpathEventEvaluator() {
    }
    
    @Override
    public boolean match(SpathQueryElement target, SpathEvent event) {
        return target.getSpathName().match(this, event);
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
            BigDecimal value = SpathUtil.getValueAsNumber(prop.getValue());
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
            Boolean value = SpathUtil.getValueAsBoolean(prop.getValue());
            if (predicate.compareTo(name, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathName target, SpathEvent event) {
        String targetValue = target.getName();
        String eventValue = event.getName();
        return targetValue.equals(eventValue);
    }

    @Override
    public boolean match(SpathFunction target, SpathEvent event) {
        String function = target.getName();
        if ("text".equals(function)) {
            return event.getText() != null;
        }
        throw new SpathQueryException("Unknown Spath function: " + function);
    }
}
