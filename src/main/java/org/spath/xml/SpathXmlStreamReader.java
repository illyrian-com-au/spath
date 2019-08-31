package org.spath.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.spath.SpathEventSource;
import org.spath.SpathStack;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventBuilder;
import org.spath.query.SpathQueryException;

public class SpathXmlStreamReader implements SpathEventSource<SpathEvent> {
    private final XMLStreamReader reader;
    
    private String text = null;
    
    public SpathXmlStreamReader(XMLStreamReader reader) {
        this.reader = reader;
    }
    
    @Override
    public boolean nextEvent(SpathStack<SpathEvent> engine) throws SpathQueryException {
        try {
            while (reader.hasNext()) {
                int event = nextEvent();
                if (event == XMLStreamReader.START_ELEMENT) {
                    engine.push(createStartEvent());
                    return true;
                } else if (event == XMLStreamReader.END_ELEMENT) {
                    engine.pop();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            throw new SpathQueryException("Could not read nextEvent", ex);
        }
    }
    
    private SpathEvent createStartEvent() {
        SpathEventBuilder builder = new SpathEventBuilder();
        QName qname = reader.getName();
        builder.withName(qname.toString());
        int size = reader.getAttributeCount();
        for (int index=0; index<size; index++) {
            String name = reader.getAttributeLocalName(index);
            String value = reader.getAttributeValue(index);
            builder.withProperty(name, value);
        }
        return builder.build();
    }
    
    @Override
    public String getText(SpathStack<SpathEvent> engine) throws SpathQueryException {
        try {
            if (text != null) {
                return text;
            } else if (reader.isStartElement()) {
                text = getText(reader);
                return text;
            } else if (reader.isEndElement()) {
                text = getText(reader);
                return text;
            }
            return "";
        } catch (XMLStreamException ex) {
            throw new SpathQueryException("Could not peek nextEvent", ex);
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
        int eventType = reader.getEventType();
        if (reader.isStartElement() || reader.isEndElement()) {
            eventType = reader.next();
        }
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
}
