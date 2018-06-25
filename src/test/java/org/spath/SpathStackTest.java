package org.spath;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.StartElement;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.xml.event.SpathXmlEventEvaluator;

public class SpathStackTest extends TestCase {
    
    SpathEvaluator<StartElement> matcher = new SpathXmlEventEvaluator(); 

    public static class EventSourceStartElement implements SpathEventSource<StartElement> {
        XMLEventFactory xmlFactory = XMLEventFactory.newFactory();

        String [] eventList;
        int index = 0;
        
        public void setList(String [] list) {
            eventList= list;
            index = 0;
        }
        
        public boolean nextEvent(SpathStack<StartElement> stack) {
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
        
        public String getText(SpathStack<StartElement> engine) {
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

    SpathStack<StartElement> spath = new SpathStack<StartElement>(matcher);
    EventSourceStartElement eventSource = new EventSourceStartElement();
    SpathEngine engine = new SpathEngineImpl<StartElement>(spath, eventSource);
    
    @Test
    public void testSimpleStartElement() {
        SpathName gwml = new SpathNameStart("GWML");
        assertEquals("/GWML", gwml.toString());

        XMLEventFactory xmlFactory = XMLEventFactory.newFactory();
        StartElement gwmlEvent = xmlFactory.createStartElement("", null, "GWML");
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
        SpathStack<StartElement> stack = new SpathStack<StartElement>(matcher);
        assertFalse("Empty list", eventSource.nextEvent(stack));
    }
    
    @Test
    public void testStreamStack() throws SpathException {
        String [] eventList = new String [] {"GWML", "header", "address", null, null, "trade", "details", null, null, null};
        eventSource.setList(eventList);
        SpathStack<StartElement> stack = new SpathStack<StartElement>(matcher);
        assertTrue("GWML", eventSource.nextEvent(stack));
        assertEquals("[<GWML>]", stack.toString());
        assertTrue("header", eventSource.nextEvent(stack));
        assertEquals("[<GWML>, <header>]", stack.toString());
        assertTrue("address", eventSource.nextEvent(stack));
        assertEquals("[<GWML>, <header>, <address>]", stack.toString());
        assertTrue("/address", eventSource.nextEvent(stack));
        assertEquals("[<GWML>, <header>]", stack.toString());
        assertTrue("/header", eventSource.nextEvent(stack));
        assertEquals("[<GWML>]", stack.toString());
        assertTrue("trade", eventSource.nextEvent(stack));
        assertEquals("[<GWML>, <trade>]", stack.toString());
        assertTrue("details", eventSource.nextEvent(stack));
        assertEquals("[<GWML>, <trade>, <details>]", stack.toString());
        assertTrue("/details", eventSource.nextEvent(stack));
        assertEquals("[<GWML>, <trade>]", stack.toString());
        assertTrue("/trade", eventSource.nextEvent(stack));
        assertEquals("[<GWML>]", stack.toString());
        assertTrue("/GWML", eventSource.nextEvent(stack));
        assertEquals("[]", stack.toString());
        assertFalse("empty", eventSource.nextEvent(stack));
    }
    
    @Test
    public void testStreamStackEngine() throws SpathException {
        String [] emptyList = new String [] {"GWML", "header", "address", null, null, "trade", "details", null, null, null};
        eventSource.setList(emptyList);
        SpathStack<StartElement> stack = new SpathStack<StartElement>(matcher);

        SpathEngine engine = new SpathEngineImpl<StartElement>(stack, eventSource);
        SpathName gwml = engine.add(new SpathNameStart("GWML"));
        SpathName header = engine.add(new SpathNameElement(gwml, "header"));
        SpathName address = engine.add(new SpathNameElement(header, "address"));
        SpathName trade = engine.add(new SpathNameElement(gwml, "trade"));
        SpathName details = engine.add(new SpathNameElement(trade, "details"));
        
        assertTrue("GWML", engine.matchNext());
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
        assertFalse("/GWML", engine.matchNext());
    }
    
    @Test
    public void testStreamStackEngineUsage() throws SpathException {
        String [] emptyList = new String [] {"GWML", "header", "address", null, null, "trade", "details", null, null, null};
        eventSource.setList(emptyList);
        SpathStack<StartElement> stack = new SpathStack<StartElement>(matcher);

        SpathEngine engine = new SpathEngineImpl<StartElement>(stack, eventSource);
        SpathName gwml = engine.add(new SpathNameStart("GWML"));
        SpathName header = engine.add(new SpathNameElement(gwml, "header"));
        SpathName address = engine.add(new SpathNameElement(header, "address"));
        SpathName trade = engine.add(new SpathNameElement(gwml, "trade"));
        SpathName details = engine.add(new SpathNameElement(trade, "details"));
        
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
