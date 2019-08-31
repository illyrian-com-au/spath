package org.spath.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;

import org.spath.SpathStack;
import org.spath.engine.SpathStreamEngineImpl;
import org.spath.engine.SpathStackImpl;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;

public class SpathXmlReaderFactory {
    
    public SpathStreamEngineImpl<SpathEvent> createEngine(XMLStreamReader reader) {
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathStack<SpathEvent> stack = new SpathStackImpl<SpathEvent>(evaluator);
        SpathXmlStreamReader stream = new SpathXmlStreamReader(reader);
        return new SpathStreamEngineImpl<SpathEvent>(stack, stream);
    }

    public SpathStreamEngineImpl<StartElement> createEngine(XMLEventReader reader) {
        SpathXmlEventReader bridge = new SpathXmlEventReader(reader);
        SpathXmlEventEvaluator evaluator = new SpathXmlEventEvaluator();
        SpathStack<StartElement> stack = new SpathStackImpl<StartElement>(evaluator);
        return new SpathStreamEngineImpl<StartElement>(stack, bridge);
    }
}
