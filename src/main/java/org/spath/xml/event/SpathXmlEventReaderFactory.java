package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import org.spath.SpathStack;
import org.spath.engine.SpathEngineImpl;
import org.spath.engine.SpathStackImpl;

public class SpathXmlEventReaderFactory {
    
    public SpathEngineImpl<StartElement> createEngine(XMLEventReader reader) {
        SpathXmlEventReader bridge = new SpathXmlEventReader(reader);
        SpathXmlEventEvaluator evaluator = new SpathXmlEventEvaluator();
        SpathStack<StartElement> stack = new SpathStackImpl<StartElement>(evaluator);
        return new SpathEngineImpl<StartElement>(stack, bridge);
    }
}
