package org.spath.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.spath.SpathEventSource;
import org.spath.SpathStack;
import org.spath.query.SpathQueryException;

import com.sun.xml.internal.stream.events.CharacterEvent;

public class SpathXmlEventReader implements SpathEventSource<StartElement> {
    private final XMLEventReader reader;
    
    public SpathXmlEventReader(XMLEventReader reader) {
        this.reader = reader;
    }
    
    @Override
    public boolean nextEvent(SpathStack<StartElement> engine) throws SpathQueryException {
        try {
            XMLEvent event;
            while (reader.hasNext()) {
                event = reader.nextEvent();
                if (event instanceof StartElement) {
                    engine.push((StartElement)event);
                    return true;
                } else if (event instanceof EndElement) {
                    engine.pop();
                    return true;
                }
            }
            return false;
        } catch (XMLStreamException ex) {
            throw new SpathQueryException("Could not read nextEvent", ex);
        }
    }

    @Override
    public String getText(SpathStack<StartElement> engine) throws SpathQueryException {
        try {
            if (reader.peek() instanceof CharacterEvent) {
                CharacterEvent event = (CharacterEvent)reader.peek();
                return event.getData();
            }
            return "";
        } catch (XMLStreamException ex) {
            throw new SpathQueryException("Could not peek nextEvent", ex);
        }
    }
}
