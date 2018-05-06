package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import org.spath.SpathEngine;
import org.spath.SpathEngineImpl;
import org.spath.SpathStack;

public class SpathXmlEventReaderFactory {
    
    SpathXmlEventReaderBridge createBridge(XMLEventReader reader) {
        return new SpathXmlEventReaderBridge(reader);
    }
    
    public SpathEngine createEngine(XMLEventReader reader) {
        SpathXmlEventReaderBridge bridge = createBridge(reader);
        SpathStack<StartElement> stack = new SpathStack<>(bridge);
        return new SpathEngineImpl<StartElement>(stack, bridge);
    }
}
