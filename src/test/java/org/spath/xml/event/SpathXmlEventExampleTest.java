package org.spath.xml.event;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathQuery;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryBuilder;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryStart;
import org.spath.query.SpathQueryType;
import org.spath.test.StringReadWriter;

import com.sun.xml.internal.stream.events.CharacterEvent;
import com.sun.xml.internal.stream.events.EndDocumentEvent;
import com.sun.xml.internal.stream.events.StartElementEvent;

public class SpathXmlEventExampleTest extends TestCase {
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlEventReaderFactory spathFactory = new SpathXmlEventReaderFactory();
    
    private SpathEngine createSpathEngine(String xml) throws XMLStreamException {
        StringReader input = new StringReader(xml);
        XMLEventReader reader = xmlFactory.createXMLEventReader(input);
        SpathEngine engine = spathFactory.createEngine(reader);
        return engine;
    }

    @Test
    public void testSimpleElement() throws Exception {
        SpathEngine engine = createSpathEngine("<data>Hello World</data>");
        String text = null;
        while (engine.matchNext()) {
            if (engine.match("/data")) {
                text = engine.getText();
            }
        }
        assertEquals("Hello World", text);
    }

    @Test
    public void testNextDataElement() throws Exception {
        SpathEngine engine = createSpathEngine("<data>Hello World</data>");
        String text = null;
        while (engine.matchNext("/data")) {
            text = engine.getText();
        }
        assertEquals("Hello World", text);
    }

    @Test
    public void testMixedText() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>Hello <b>World</b></data>");
        out.close();
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = spathFactory.createEngine(reader);
        SpathQuery data = engine.query(new SpathQueryStart("data"));
        SpathQuery bold = engine.query(new SpathQueryElement(data, "b"));

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
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = spathFactory.createEngine(reader);
        assertTrue("matchNext()", engine.matchNext("data"));
        assertTrue("match(data)", engine.match("data[@lang='En']"));
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
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = spathFactory.createEngine(reader);
        
        String name = null;
        String address = null;
        String price = null;

        while (engine.matchNext("/data")) {
            if (engine.match("name")) {
                name = engine.getText();
            }
            if (engine.match("address")) {
                address = engine.getText();
            }
            if (engine.match("trade/detail/price")) {
                price = engine.getText();
            }
        }
        
        assertEquals("John Doe", name);
        assertEquals("1 Erehwon St, Elsewhere", address);
        assertEquals("12.34", price);
    }
}
