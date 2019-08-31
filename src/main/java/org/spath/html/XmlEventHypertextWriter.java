package org.spath.html;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.spath.SpathEngine;
import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.engine.SpathEngineImpl;
import org.spath.engine.SpathStackImpl;
import org.spath.xml.SpathXmlEventEvaluator;

public class XmlEventHypertextWriter extends XmlEventWriterDelegate implements SpathEngine {
    private final PrintWriter out;
    private final SpathStack<StartElement> stack;
    private final SpathEngineImpl<StartElement> engine;
    Map<SpathMatch, String>targetMap = new HashMap<SpathMatch, String>();
    
    public XmlEventHypertextWriter(XMLEventWriter delegate, PrettyPrintWriter pretty) {
        super(delegate);
        out = pretty;
        engine = createEngine();
        stack = engine.getStack();
    }
    
    private SpathEngineImpl<StartElement> createEngine() {
        SpathXmlEventEvaluator eval = new SpathXmlEventEvaluator();
        SpathStack<StartElement> stack = new SpathStackImpl<StartElement>(eval);
        SpathEngineImpl<StartElement> engine = new SpathEngineImpl<StartElement>(stack);
        return engine;
    }
    
    public void apply(Characters event) throws XMLStreamException {
        SpathMatch match = findMatch();
        String target = this.getTarget(match);
        if (match != null) {
            out.print("<a href=\"#");
            out.print(target);
            out.print("\">");
            super.apply(event);
            out.print("</a>");
        } else {
            super.apply(event);
        }
    }

    @Override
    public void apply(StartElement event) throws XMLStreamException {
        stack.push(event);
        super.apply(event);
    }
    
    @Override
    public void apply(EndElement event) throws XMLStreamException {
        stack.pop();
        super.apply(event);
    }

    @Override
    public SpathMatch findMatch() {
        return engine.findMatch();
    }

    @Override
    public boolean match(SpathQuery query) {
        return engine.match(query);
    }

    @Override
    public boolean match(String expr) {
        return engine.match(expr);
    }

    @Override
    public SpathQuery add(SpathQuery query) {
        return engine.add(query);
    }

    @Override
    public SpathQuery add(String expr) {
        return engine.add(expr);
    }
    
    void addTarget(String target, String spath) {
        SpathQuery query = engine.add(spath);
        targetMap.put(query, target);
        
    }

    String getTarget(SpathMatch query) {
        return targetMap.get(query);
    }
}
