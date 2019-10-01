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

public class SpathXmlEventReader extends SpathXmlEventEvaluator implements SpathEventSource<StartElement> {
    private final XMLEventReader reader;
    private final SpathStack<StartElement> stack;
    
    public SpathXmlEventReader(XMLEventReader reader, SpathStack<StartElement> stack) {
        this.reader = reader;
        this.stack = stack;
    }
    
    @Override
    public boolean nextEvent() throws SpathQueryException {
        try {
            XMLEvent event;
            while (reader.hasNext()) {
                event = reader.nextEvent();
                if (event instanceof StartElement) {
                    stack.push((StartElement)event);
                    return true;
                } else if (event instanceof EndElement) {
                    stack.pop();
                    return true;
                }
            }
            return false;
        } catch (XMLStreamException ex) {
            throw new SpathQueryException("Could not read nextEvent", ex);
        }
    }

    @Override
    public String getText() throws SpathQueryException {
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
