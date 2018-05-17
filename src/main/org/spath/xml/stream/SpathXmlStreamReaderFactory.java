package org.spath.xml.stream;

import javax.xml.stream.XMLStreamReader;

import org.spath.SpathEngine;
import org.spath.SpathEngineImpl;
import org.spath.SpathStack;
import org.spath.data.SpathEvent;
import org.spath.data.SpathEventEvaluator;

public class SpathXmlStreamReaderFactory {
    
    public SpathEngine createEngine(XMLStreamReader reader) {
        SpathXmlStreamReader bridge = new SpathXmlStreamReader(reader);
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathStack<SpathEvent> stack = new SpathStack<>(evaluator);
        return new SpathEngineImpl<SpathEvent>(stack, bridge);
    }
}
