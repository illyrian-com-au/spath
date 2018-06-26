package org.spath.xml.stream;

import javax.xml.stream.XMLStreamReader;

import org.spath.SpathEngine;
import org.spath.SpathStack;
import org.spath.engine.SpathEngineImpl;
import org.spath.engine.SpathStackImpl;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;

public class SpathXmlStreamReaderFactory {
    
    public SpathEngine createEngine(XMLStreamReader reader) {
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathStack<SpathEvent> stack = new SpathStackImpl<SpathEvent>(evaluator);
        SpathXmlStreamReader stream = new SpathXmlStreamReader(reader);
        return new SpathEngineImpl<SpathEvent>(stack, stream);
    }
}
