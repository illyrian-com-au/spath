package org.spath.html;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.parser.SpathParser;

@Deprecated
public class SpathXmlEventWriter extends XmlEventWriterDelegate {
    private final SpathStack<StartElement> stack;
    final Map<String, SpathQuery> pathMap;
    final SpathParser parser = new SpathParser();

    public SpathXmlEventWriter(XMLEventWriter writer, SpathStack<StartElement> stack) {
        super(writer);
        this.stack = stack;
        pathMap = new HashMap<String, SpathQuery>();
    }
    
    public SpathMatch findMatch() {
        for (SpathQuery target : pathMap.values()) {
            if (stack.match(target)) {
                return target;
            }
        }
        return null;
    }
    
    public SpathQuery get(String key) {
        return pathMap.get(key);
    }
    
    public SpathQuery query(String expr) {
        SpathQuery query = get(expr);
        if (query == null) {
            query = add(expr);
        }
        return query;
    }
    
    public SpathQuery add(String expr) {
        SpathQuery query = parser.parse(expr);
        add(expr, query);
        return query;
    }
    
    public SpathQuery add(String key, SpathQuery query) {
        pathMap.put(key, query);
        return query;
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
}
