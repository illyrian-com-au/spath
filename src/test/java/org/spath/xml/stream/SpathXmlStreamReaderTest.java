package org.spath.xml.stream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathQuery;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryRelative;
import org.spath.query.SpathQueryStart;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateString;
import org.spath.test.StringReadWriter;

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
        SpathQuery data = new SpathQueryStart("data");
        engine.query(data);
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
        
        XMLStreamReader reader = xmlFactory.createXMLStreamReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathQueryStart data = new SpathQueryStart("data");
        data.add(new SpathPredicateString("lang", SpathPredicateOperator.EQ, "En"));
        engine.query(data);
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
        SpathQuery dataPath = engine.query(new SpathQueryStart("data"));
        SpathQuery namePath = engine.query(new SpathQueryRelative("name"));
        SpathQuery addressPath = engine.query(new SpathQueryRelative("address"));
        SpathQuery pricePath = engine.query(new SpathQueryRelative("price"));
        
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
