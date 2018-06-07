package org.spath.xml.event;

import java.math.BigDecimal;
import java.util.Iterator;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.spath.SpathEvaluator;
import org.spath.SpathNameElement;
import org.spath.SpathPredicateBoolean;
import org.spath.SpathPredicateNumber;
import org.spath.SpathPredicateString;

public class SpathXmlEventEvaluator implements SpathEvaluator<StartElement> {
    public SpathXmlEventEvaluator() {
    }
    
    @Override
    public boolean match(SpathNameElement target, StartElement event) {
        String targetValue = target.getName();
        String eventValue = event.getName().toString();
        return targetValue.equals(eventValue);
    }
    
    @Override
    public boolean match(SpathPredicateString predicate, StartElement event) {
        @SuppressWarnings("unchecked")
        Iterator<Attribute> iter = event.getAttributes();
        while (iter.hasNext()) {
            Attribute attr = iter.next();
            String name = attr.getName().toString();
            String value = attr.getValue();
            if (predicate.compareTo(name, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathPredicateNumber predicate, StartElement event) {
        @SuppressWarnings("unchecked")
        Iterator<Attribute> iter = event.getAttributes();
        while (iter.hasNext()) {
            Attribute attr = iter.next();
            String name = attr.getName().toString();
            String value = attr.getValue();
            BigDecimal decimal = predicate.getValueAsNumber(value);
            if (decimal != null && predicate.compareTo(name, decimal)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathPredicateBoolean predicate, StartElement event) {
        @SuppressWarnings("unchecked")
        Iterator<Attribute> iter = event.getAttributes();
        while (iter.hasNext()) {
            Attribute attr = iter.next();
            String name = attr.getName().toString();
            String value = attr.getValue();
            Boolean bool = predicate.getValueAsBoolean(value);
            if (predicate.compareTo(name, bool)) {
                return true;
            }
        }
        return false;
    }
}
