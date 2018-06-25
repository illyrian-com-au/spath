package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import org.spath.SpathEngine;
import org.spath.SpathEngineImpl;
import org.spath.SpathStack;

public class SpathXmlEventReaderFactory {
    
    public SpathEngine createEngine(XMLEventReader reader) {
        SpathXmlEventReader bridge = new SpathXmlEventReader(reader);
        SpathXmlEventEvaluator evaluator = new SpathXmlEventEvaluator();
        SpathStack<StartElement> stack = new SpathStack<StartElement>(evaluator);
        return new SpathEngineImpl<StartElement>(stack, bridge);
    }
}
