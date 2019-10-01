package org.spath.xml.stream;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathStreamEngine;
import org.spath.SpathQuery;
import org.spath.query.SpathName;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryException;
import org.spath.query.SpathQueryRelative;
import org.spath.query.SpathQueryStart;
import org.spath.test.StringReadWriter;
import org.spath.xml.SpathXmlReaderFactory;

public class SpathXmlStreamReaderTest extends TestCase {
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlReaderFactory spathFactory = new SpathXmlReaderFactory();
    
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
        SpathStreamEngine engine = spathFactory.createEngine(reader);
        SpathQuery data = new SpathQueryStart(new SpathName("data"));
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
        out.println("<data>Hello <b>World</b> Goodbye</data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathStreamEngine engine = spathFactory.createEngine(reader);
        SpathQuery data = engine.add(new SpathQueryStart(new SpathName("data")));
        SpathQuery bold = engine.add(new SpathQueryElement(data, new SpathName("b")));

        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertEquals("Hello ", engine.getText());
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(bold)", engine.match(bold));
        assertEquals("World", engine.getText());
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertEquals(" Goodbye", engine.getText());
        assertEquals(" Goodbye", engine.getText());
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testSimpleAttribute() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data lang='En' type='string' >Hello World</data>");
        out.close();
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathStreamEngine engine = spathFactory.createEngine(reader);
        SpathQueryStart data = new SpathQueryStart(new SpathName("data"));
        data.add(new SpathPredicateString("lang", SpathPredicateOperator.EQ, "En"));
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
        SpathStreamEngine engine = spathFactory.createEngine(reader);
        SpathQuery dataPath = engine.add(new SpathQueryStart(new SpathName("data")));
        SpathQuery namePath = engine.add(new SpathQueryRelative(new SpathName("name")));
        SpathQuery addressPath = engine.add(new SpathQueryRelative(new SpathName("address")));
        SpathQuery pricePath = engine.add(new SpathQueryRelative(new SpathName("price")));
        
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
    
    @Test
    public void testNoQueryException() throws Exception {
        StringReader input = new StringReader("<greeting/>");
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(input);
        SpathStreamEngine engine = spathFactory.createEngine(reader);
        try {
            engine.matchNext();
            engine.matchNext();
            fail("Should throw SpathQueryException");
        } catch (SpathQueryException ex) {
            assertEquals("No queries have been added to the SpathStreamEngine", ex.getMessage());
        }
    }
    
    @Test
    public void testInfiniteLoopException() throws Exception {
        StringReader input = new StringReader("<data><item/></data>");
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(input);
        SpathStreamEngine engine = spathFactory.createEngine(reader).withNoProgressThreshold(100);
        try {
            int count = 0;
            while (engine.matchNext("/data")) {
                // the following statement causes an infinite loop new events are not being read.
                while (engine.match("item")) {
                    if (++count > 100) {
                        fail("Infinite loop not detected, count=" + count);
                    }
                }
            }
        } catch (SpathQueryException ex) {
            assertEquals("Infinite loop detected. Do not use while (match(query)) ...", ex.getMessage());
        }
    }
}
