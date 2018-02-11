package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.spath.SpathEvaluator;
import org.spath.SpathEventSource;
import org.spath.SpathException;
import org.spath.SpathNameImpl;
import org.spath.SpathNameStar;
import org.spath.SpathPredicate;
import org.spath.SpathStack;

import com.sun.xml.internal.stream.events.CharacterEvent;

public class SpathXmlEventReaderBridge implements SpathEvaluator<StartElement>, SpathEventSource<StartElement> {
    private final XMLEventReader reader;
    
    public SpathXmlEventReaderBridge(XMLEventReader reader) {
        this.reader = reader;
    }
    
    @Override
    public boolean match(SpathNameImpl target, StartElement event) {
        String targetValue = target.getValue();
        String eventValue = event.getName().toString();
        return targetValue.equals(eventValue);
    }
    
    @Override
    public boolean match(SpathNameStar target, StartElement event) {
        return true;
    }
    
    @Override
    public boolean match(SpathPredicate target, StartElement event) {
        return false;
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
                return reader.peek().toString();
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
