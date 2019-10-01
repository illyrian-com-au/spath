package org.spath.xml;

import java.math.BigDecimal;
import java.util.Iterator;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.spath.SpathEvaluator;
import org.spath.engine.SpathUtil;
import org.spath.query.SpathFunction;
import org.spath.query.SpathName;
import org.spath.query.SpathPredicateBoolean;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryElement;

public class SpathXmlEventEvaluator implements SpathEvaluator<StartElement> {
    public SpathXmlEventEvaluator() {
    }
    
    @Override
    public boolean match(SpathQueryElement target, StartElement event) {
        String targetValue = target.getSpathName().getName();
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
            BigDecimal decimal = SpathUtil.getValueAsNumber(value);
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
            Boolean bool = SpathUtil.getValueAsBoolean(value);
            if (predicate.compareTo(name, bool)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathName target, StartElement event) {
        String targetValue = target.getName();
        String eventValue = event.getName().getLocalPart();
        return targetValue.equals(eventValue);
    }

    @Override
    public boolean match(SpathFunction target, StartElement event) {
        String targetValue = target.getName();
        if ("text()".equals(targetValue)) {
            return getText() != null; // FIXME
        }
        return false;
    }
    
    public String getText() {
        return null;
    }
}
