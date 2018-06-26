package org.spath.xml.event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathQuery;
import org.spath.query.SpathQueryBuilder;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryRelative;
import org.spath.query.SpathQueryStart;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryType;
import org.spath.test.StringReadWriter;

import com.sun.xml.internal.stream.events.CharacterEvent;
import com.sun.xml.internal.stream.events.EndDocumentEvent;
import com.sun.xml.internal.stream.events.StartElementEvent;

public class SpathXmlEventReaderTest extends TestCase {
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlEventReaderFactory factory = new SpathXmlEventReaderFactory();
    
    @Test
    public void testXmlEventReader() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>Hello World</data>");
        out.close();
        
        XMLEvent event;
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        assertTrue("hasNext", reader.hasNext());
        event = reader.nextTag();
        assertEquals(StartElementEvent.class, event.getClass());
        assertEquals("<data>", event.toString());
        assertEquals(CharacterEvent.class, reader.peek().getClass());
        String text = reader.getElementText();
        assertEquals("Hello World", text);

        assertEquals(EndDocumentEvent.class, reader.nextEvent().getClass());
        assertFalse("End of document", reader.hasNext());
    }

    @Test
    public void testSimpleElement() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>Hello World</data>");
        out.close();
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathQuery data = new SpathQueryStart("data");
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
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathQuery data = engine.add(new SpathQueryStart("data"));
        SpathQuery bold = engine.add(new SpathQueryElement(data, "b"));

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
        SpathEngine engine = factory.createEngine(reader);
        SpathQueryStart data = new SpathQueryStart("data");
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
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = factory.createEngine(reader);
        SpathQueryBuilder builder = new SpathQueryBuilder();
        SpathQuery dataPath = engine.add(builder.withType(SpathQueryType.ROOT).withName("data").build());
        SpathQuery namePath = engine.add(builder.withType(SpathQueryType.RELATIVE).withName("name").build());
        SpathQuery addressPath = engine.add(builder.withType(SpathQueryType.RELATIVE).withName("address").build());
        SpathQuery pricePath = engine.add(builder.withType(SpathQueryType.RELATIVE).withName("price").build());
        
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
