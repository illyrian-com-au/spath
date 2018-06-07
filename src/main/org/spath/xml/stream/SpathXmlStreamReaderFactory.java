package org.spath.xml.stream;

import javax.xml.stream.XMLStreamReader;

import org.spath.SpathEngine;
import org.spath.SpathEngineImpl;
import org.spath.SpathStack;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;

public class SpathXmlStreamReaderFactory {
    
    public SpathEngine createEngine(XMLStreamReader reader) {
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathStack<SpathEvent> stack = new SpathStack<>(evaluator);
        SpathXmlStreamReader stream = new SpathXmlStreamReader(reader);
        return new SpathEngineImpl<SpathEvent>(stack, stream);
    }
}
