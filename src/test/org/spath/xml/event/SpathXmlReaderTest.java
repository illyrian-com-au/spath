package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathName;
import org.spath.SpathNameImpl;
import org.spath.StringReadWriter;

import com.sun.xml.internal.stream.events.CharacterEvent;
import com.sun.xml.internal.stream.events.EndDocumentEvent;
import com.sun.xml.internal.stream.events.StartElementEvent;

public class SpathXmlReaderTest extends TestCase {
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlEventReaderFactory factory = new SpathXmlEventReaderFactory();
    
    @Test
    public void testXmlEventReader() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<gwml>Hello World</gwml>");
        out.close();
        
        XMLEvent event;
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        assertTrue("hasNext", reader.hasNext());
        event = reader.nextTag();
        assertEquals(StartElementEvent.class, event.getClass());
        assertEquals("<gwml>", event.toString());
        assertEquals(CharacterEvent.class, reader.peek().getClass());
        String text = reader.getElementText();
        assertEquals("Hello World", text);

        assertEquals(EndDocumentEvent.class, reader.nextEvent().getClass());
        assertFalse("End of document", reader.hasNext());
    }

    @Test
    public void testSimpleProperty() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<gwml>Hello World</gwml>");
        out.close();
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathName gwml = new SpathNameImpl("gwml");
        engine.add(gwml);
        assertTrue("matchAny()", engine.matchAny());
        assertTrue("match(gwml)", engine.match(gwml));
        assertEquals("Hello World", engine.getText());
        // Check that getText does not change the state
        assertEquals("Hello World", engine.getText());
        assertFalse("End of input", engine.matchAny());
    }

    @Test
    public void testMixedText() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<gwml>Hello <b>World</b></gwml>");
        out.close();
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathName gwml = engine.add(new SpathNameImpl("gwml"));
        SpathName bold = engine.add(new SpathNameImpl(gwml, "b"));

        assertTrue("matchAny()", engine.matchAny());
        assertTrue("match(gwml)", engine.match(gwml));
        assertEquals("Hello ", engine.getText());
        assertTrue("matchAny()", engine.matchAny());
        assertTrue("match(bold)", engine.match(bold));
        assertEquals("World", engine.getText());
        assertFalse("End of input", engine.matchAny());
    }

    @Test
    public void testNestedExample() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<gwml>");
        out.println("<header>");
        out.println("<name>John Doe</name>");
        out.println("<address>1 Erehwon St, Elsewhere</address>");
        out.println("</header>");
        out.println("<trade>");
        out.println("<detail>");
        out.println("<price>12.34</price>");
        out.println("<currency>USD</currency>");
        out.println("<commodity>Pork Bellies</commodity>");
        out.println("<date>2018-03-02</date>");
        out.println("</detail>");
        out.println("</trade>");
        out.println("</gwml>");
        out.close();
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathName gwmlPath = engine.add(new SpathNameImpl("gwml"));
        SpathName namePath = engine.add(new SpathNameImpl("name", -1));
        SpathName addressPath = engine.add(new SpathNameImpl("address", -1));
        SpathName pricePath = engine.add(new SpathNameImpl("price", -1));
        
        String name = null;
        String address = null;
        String price = null;

        while (engine.matchAny(gwmlPath)) {
            if (engine.match(namePath)) {
                name = engine.getText();
            }
            if (engine.match(addressPath)) {
                address = engine.getText();
            }
            if (engine.match(pricePath)) {
                price = engine.getText();
            }
        }
        
        assertEquals("John Doe", name);
        assertEquals("1 Erehwon St, Elsewhere", address);
        assertEquals("12.34", price);
    }
}
