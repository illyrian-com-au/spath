package org.spath.xml.event;

import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.spath.SpathEvaluator;
import org.spath.SpathEventSource;
import org.spath.SpathException;
import org.spath.SpathNameElement;
import org.spath.SpathNameRelative;
import org.spath.SpathNameStar;
import org.spath.SpathPredicateBoolean;
import org.spath.SpathPredicateNumber;
import org.spath.SpathPredicateString;
import org.spath.SpathStack;

import com.sun.xml.internal.stream.events.CharacterEvent;

public class SpathXmlEventReaderBridge implements SpathEvaluator<StartElement>, SpathEventSource<StartElement> {
    private final XMLEventReader reader;
    
    public SpathXmlEventReaderBridge(XMLEventReader reader) {
        this.reader = reader;
    }
    
    @Override
    public boolean match(SpathNameElement target, StartElement event) {
        String targetValue = target.getName();
        String eventValue = event.getName().toString();
        return targetValue.equals(eventValue);
    }
    
    @Override
    public boolean match(SpathNameRelative target, StartElement event) {
        String targetValue = target.getName();
        String eventValue = event.getName().toString();
        return targetValue.equals(eventValue);
    }

    @Override
    public boolean match(SpathNameStar target, StartElement event) {
        return true;
    }
    
    @Override
    public boolean match(SpathPredicateString predicate, StartElement event) {
        @SuppressWarnings("unchecked")
        Iterator<Attribute> iter = event.getAttributes();
        while (iter.hasNext()) {
            Attribute attr = iter.next();
            if (matchAttribute(predicate, attr)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchAttribute(SpathPredicateString predicate, Attribute attribute) {
        if (predicate.getName().equals(attribute.getName().toString())) {
            if (predicate.getOperator() != null) {
                if (predicate.getValue().equals(attribute.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean match(SpathPredicateNumber target, StartElement event) {
        throw new SpathException("SpathPredicateNumber not handled");
    }

    @Override
    public boolean match(SpathPredicateBoolean target, StartElement event) {
        throw new SpathException("SpathPredicateBoolean not handled");
    }

    @Override
    public boolean nextEvent(SpathStack<StartElement> engine) throws SpathException {
        try {
            XMLEvent event;
            while (reader.hasNext()) {
                event = reader.nextEvent();
                if (event instanceof StartElement) {
                    engine.push((StartElement)event);
                    return true;
                } else if (event instanceof EndElement) {
                    engine.pop();
                }
            }
            return false;
        } catch (XMLStreamException ex) {
            throw new SpathException("Could not read nextEvent", ex);
        }
    }

    @Override
    public String getText(SpathStack<StartElement> engine) throws SpathException {
        try {
            if (reader.peek() instanceof CharacterEvent) {
                CharacterEvent event = (CharacterEvent)reader.peek();
                return event.getData();
            }
            return "";
        } catch (XMLStreamException ex) {
            throw new SpathException("Could not peek nextEvent", ex);
        }
    }
    
    @Override
    public boolean hasNext() {
        return reader.hasNext();
    }
}
