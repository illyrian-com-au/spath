package org.spath.xml.stream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.spath.SpathEventSource;
import org.spath.SpathException;
import org.spath.SpathStack;
import org.spath.data.SpathEvent;

public class SpathXmlStreamReader implements SpathEventSource<SpathEvent> {
    private final XMLStreamReader reader;
    
    private String text = null;
    
    public SpathXmlStreamReader(XMLStreamReader reader) {
        this.reader = reader;
    }
    
    @Override
    public boolean nextEvent(SpathStack<SpathEvent> engine) throws SpathException {
        try {
            while (reader.hasNext()) {
                int event = nextEvent();
                if (event == XMLStreamReader.START_ELEMENT) {
                    QName name = reader.getName();
                    SpathEvent element = new SpathEvent(name.toString());
                    populateEvent(element);
                    engine.push(element);
                    return true;
                } else if (event == XMLStreamReader.END_ELEMENT) {
                    engine.pop();
                }
            }
            return false;
        } catch (Exception ex) {
            throw new SpathException("Could not read nextEvent", ex);
        }
    }
    
    @Override
    public String getText(SpathStack<SpathEvent> engine) throws SpathException {
        try {
            if (text != null) {
                return text;
            } else if (reader.isStartElement()) {
                text = getText(reader);
                SpathEvent element = engine.peek();
                element.setText(text);
                return text;
            }
            return "";
        } catch (XMLStreamException ex) {
            throw new SpathException("Could not peek nextEvent", ex);
        }
    }
    
    private void populateEvent(SpathEvent event) {
        int size = reader.getAttributeCount();
        for (int index=0; index<size; index++) {
            String name = reader.getAttributeLocalName(index);
            String value = reader.getAttributeValue(index);
            event.addProperty(name, value);
        }
    }
    
    private int nextEvent() throws XMLStreamException {
        int event;
        if (text != null) {
            // Return the event that terminated the text
            event = reader.getEventType();
            text = null;
        } else {
            event = reader.next();
        }
        return event;
    }

    private String getText(XMLStreamReader reader) throws XMLStreamException {
        StringBuffer buf = new StringBuffer();
        int eventType = reader.next();
        while(true) {
            if(eventType == XMLStreamConstants.CHARACTERS ||
                    eventType == XMLStreamConstants.CDATA ||
                    eventType == XMLStreamConstants.SPACE ||
                    eventType == XMLStreamConstants.ENTITY_REFERENCE) {
                buf.append(reader.getText());
            } else if(eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                    || eventType == XMLStreamConstants.COMMENT) {
                // skipping
            } else {
                break;
            }
            eventType = reader.next();
        }
        return buf.toString();
    }
    
    @Override
    public boolean hasNext() throws SpathException {
        try {
            return reader.hasNext();
        } catch (Exception ex) {
            throw new SpathException("Could not call hasNext()", ex);
        }
    }
}
