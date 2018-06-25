package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.spath.SpathEventSource;
import org.spath.SpathException;
import org.spath.SpathStack;

import com.sun.xml.internal.stream.events.CharacterEvent;

public class SpathXmlEventReader implements SpathEventSource<StartElement> {
    private final XMLEventReader reader;
    
    public SpathXmlEventReader(XMLEventReader reader) {
        this.reader = reader;
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
}
