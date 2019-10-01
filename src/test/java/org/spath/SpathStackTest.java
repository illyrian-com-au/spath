package org.spath;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.StartElement;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.engine.SpathStreamEngineImpl;
import org.spath.engine.SpathStackImpl;
import org.spath.query.SpathName;
import org.spath.query.SpathQueryException;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryStart;
import org.spath.xml.SpathXmlEventEvaluator;

public class SpathStackTest extends TestCase {
    
    SpathEvaluator<StartElement> matcher = new SpathXmlEventEvaluator(); 
    SpathStack<StartElement> stack = new SpathStackImpl<StartElement>(matcher);

    public class EventSourceStartElement implements SpathEventSource {
        XMLEventFactory xmlFactory = XMLEventFactory.newFactory();

        String [] eventList;
        int index = 0;
        
        public void setList(String [] list) {
            eventList= list;
            index = 0;
        }
        
        public boolean nextEvent() {
            if (index < eventList.length) {
                String value = eventList[index++];
                if (value == null) {
                    stack.pop();
                } else {
                    StartElement event = createStartElement(value);
                    stack.push(event);
                }
                return true;
            } else {
                return false;
            }
        }
        
        public String getText() {
            return "";
        }
        
        public boolean hasNext() {
            return index < eventList.length;
        }

        StartElement createStartElement(String localName) {
            String prefix = "";
            String namespaceUri = null;
            StartElement gwmlEvent = xmlFactory.createStartElement(prefix, namespaceUri, localName);
            return gwmlEvent;
        }
    };

    SpathStack<StartElement> spath = new SpathStackImpl<StartElement>(matcher);
    EventSourceStartElement eventSource = new EventSourceStartElement();
    SpathStreamEngine engine = new SpathStreamEngineImpl<StartElement>(spath, eventSource);
    
    @Test
    public void testSimpleStartElement() {
        SpathQuery gwml = new SpathQueryStart(new SpathName("FpML"));
        assertEquals("/FpML", gwml.toString());

        XMLEventFactory xmlFactory = XMLEventFactory.newFactory();
        StartElement gwmlEvent = xmlFactory.createStartElement("", null, "FpML");
        assertFalse("Should not match " + gwml, spath.match(gwml));
        spath.push(gwmlEvent);
        assertTrue("Should match " + gwml, spath.match(gwml));
        spath.pop();
        assertFalse("Should not match " + gwml, spath.match(gwml));
    }
    
    @Test
    public void testEmptyStreamStack() {
        String [] emptyList = new String [] {};
        eventSource.setList(emptyList);
        assertFalse("Empty list", eventSource.nextEvent());
    }
    
    @Test
    public void testStreamStack() throws SpathQueryException {
        String [] eventList = new String [] {"FpML", "header", "address", null, null, "trade", "details", null, null, null};
        eventSource.setList(eventList);
        assertTrue("FpML", eventSource.nextEvent());
        assertEquals("[<FpML>]", stack.toString());
        assertTrue("header", eventSource.nextEvent());
        assertEquals("[<FpML>, <header>]", stack.toString());
        assertTrue("address", eventSource.nextEvent());
        assertEquals("[<FpML>, <header>, <address>]", stack.toString());
        assertTrue("/address", eventSource.nextEvent());
        assertEquals("[<FpML>, <header>]", stack.toString());
        assertTrue("/header", eventSource.nextEvent());
        assertEquals("[<FpML>]", stack.toString());
        assertTrue("trade", eventSource.nextEvent());
        assertEquals("[<FpML>, <trade>]", stack.toString());
        assertTrue("details", eventSource.nextEvent());
        assertEquals("[<FpML>, <trade>, <details>]", stack.toString());
        assertTrue("/details", eventSource.nextEvent());
        assertEquals("[<FpML>, <trade>]", stack.toString());
        assertTrue("/trade", eventSource.nextEvent());
        assertEquals("[<FpML>]", stack.toString());
        assertTrue("/FpML", eventSource.nextEvent());
        assertEquals("[]", stack.toString());
        assertFalse("empty", eventSource.nextEvent());
    }
    
    @Test
    public void testStreamStackEngine() throws SpathQueryException {
        String [] eventList = new String [] {"FpML", "header", "address", null, null, "trade", "details", null, null, null};
        eventSource.setList(eventList);

        SpathStreamEngine engine = new SpathStreamEngineImpl<StartElement>(stack, eventSource);
        SpathQuery gwml = engine.add(new SpathQueryStart(new SpathName("FpML")));
        SpathQuery header = engine.add(new SpathQueryElement(gwml, new SpathName("header")));
        SpathQuery address = engine.add(new SpathQueryElement(header, new SpathName("address")));
        SpathQuery trade = engine.add(new SpathQueryElement(gwml, new SpathName("trade")));
        SpathQuery details = engine.add(new SpathQueryElement(trade, new SpathName("details")));
        
        assertTrue("FpML", engine.matchNext());
        assertTrue("Match: " + gwml, engine.match(gwml));
        assertTrue("header", engine.matchNext());
        assertTrue("Match: " + header, engine.match(header));
        assertTrue("address", engine.matchNext());
        assertTrue("Match: " + address, engine.match(address));
        assertTrue("/address", engine.matchNext());
        assertTrue("Match: " + header, engine.match(header));
        assertTrue("/header", engine.matchNext());
        assertTrue("Match: " + gwml, engine.match(gwml));
        assertTrue("trade", engine.matchNext());
        assertTrue("Match: " + trade, engine.match(trade));
        assertTrue("details", engine.matchNext());
        assertTrue("Match: " + details, engine.match(details));
        assertTrue("/details", engine.matchNext());
        assertTrue("Match: " + trade, engine.match(trade));
        assertTrue("/trade", engine.matchNext());
        assertTrue("Match: " + trade, engine.match(gwml));
        assertFalse("/FpML", engine.matchNext());
    }
    
    @Test
    public void testStreamStackEngineUsage() throws SpathQueryException {
        String [] eventList = new String [] {"FpML", "header", "address", null, null, "trade", "details", null, null, null};
        eventSource.setList(eventList);

        SpathStreamEngine engine = new SpathStreamEngineImpl<StartElement>(stack, eventSource);
        SpathQuery gwml = engine.add(new SpathQueryStart(new SpathName("FpML")));
        SpathQuery header = engine.add(new SpathQueryElement(gwml, new SpathName("header")));
        SpathQuery address = engine.add(new SpathQueryElement(header, new SpathName("address")));
        SpathQuery trade = engine.add(new SpathQueryElement(gwml, new SpathName("trade")));
        SpathQuery details = engine.add(new SpathQueryElement(trade, new SpathName("details")));
        
        boolean hasAddress = false;
        boolean hasDetails = false;
        while (engine.matchNext()) {
            if (engine.match(address)) {
                hasAddress = true;
            }
            if (engine.match(details)) {
                hasDetails = true;
            }
        }
        assertTrue("hasAddress", hasAddress);
        assertTrue("hasAddress", hasDetails);
    }
}
