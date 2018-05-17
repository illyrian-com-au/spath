package org.spath.xml.stream;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathName;
import org.spath.SpathNameElement;
import org.spath.SpathNameRelative;
import org.spath.SpathNameStart;
import org.spath.SpathOperator;
import org.spath.SpathPredicateString;
import org.spath.test.StringReadWriter;

import com.sun.xml.internal.stream.events.CharacterEvent;
import com.sun.xml.internal.stream.events.EndDocumentEvent;
import com.sun.xml.internal.stream.events.StartElementEvent;

public class SpathXmlStreamReaderTest extends TestCase {
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlStreamReaderFactory factory = new SpathXmlStreamReaderFactory();
    
    @Test
    public void testXmlStreamReader() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>Hello World</data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        assertTrue("hasNext", reader.hasNext());
        assertEquals(XMLStreamReader.START_ELEMENT, reader.next());
        assertEquals("data", reader.getLocalName());
        assertEquals("Hello World", reader.getElementText());
        assertEquals(XMLStreamReader.END_ELEMENT, reader.getEventType());

        assertEquals(XMLStreamReader.END_DOCUMENT, reader.next());
        assertFalse("End of document", reader.hasNext());
    }

    @Test
    public void testSimpleElement() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>Hello World</data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathName data = new SpathNameStart("data");
        engine.add(data);
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertEquals("Hello World", engine.getText());
        // Check that getText does not change the state
        assertEquals("Hello World", engine.getText());
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testMixedText() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>Hello <b>World</b></data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathName data = engine.add(new SpathNameStart("data"));
        SpathName bold = engine.add(new SpathNameElement(data, "b"));

        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertEquals("Hello ", engine.getText());
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(bold)", engine.match(bold));
        assertEquals("World", engine.getText());
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testSimpleAttribute() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data lang='En' type='string' >Hello World</data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathNameStart data = new SpathNameStart("data");
        data.add(new SpathPredicateString("lang", SpathOperator.EQ, "En"));
        engine.add(data);
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertEquals("Hello World", engine.getText());
        // Check that getText does not change the state
        assertEquals("Hello World", engine.getText());
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testNestedExample() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>");
        out.println("  <header>");
        out.println("    <name>John Doe</name>");
        out.println("    <address>1 Erehwon St, Elsewhere</address>");
        out.println("  </header>");
        out.println("  <trade>");
        out.println("    <detail>");
        out.println("      <price>12.34</price>");
        out.println("      <currency>USD</currency>");
        out.println("      <commodity>Pork Bellies</commodity>");
        out.println("      <date>2018-03-02</date>");
        out.println("    </detail>");
        out.println("  </trade>");
        out.println("</data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathName dataPath = engine.add(new SpathNameStart("data"));
        SpathName namePath = engine.add(new SpathNameRelative("name"));
        SpathName addressPath = engine.add(new SpathNameRelative("address"));
        SpathName pricePath = engine.add(new SpathNameRelative("price"));
        
        String name = null;
        String address = null;
        String price = null;

        while (engine.matchNext(dataPath)) {
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
