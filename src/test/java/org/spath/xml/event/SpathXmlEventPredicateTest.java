package org.spath.xml.event;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathName;
import org.spath.SpathStack;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;
import org.spath.parser.SpathParser;

public class SpathXmlEventPredicateTest extends TestCase {
    SpathParser parser = new SpathParser();
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlEventReaderFactory factory = new SpathXmlEventReaderFactory();
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(evaluator);
    
    private SpathEngine createSpathEngine(String xml) throws XMLStreamException {
        StringReader input = new StringReader(xml);
        XMLEventReader reader = xmlFactory.createXMLEventReader(input);
        SpathEngine engine = factory.createEngine(reader);
        return engine;
    }
    
    @Test
    public void testAttributeEquals() throws XMLStreamException {
        SpathName amount = parser.parse("amount[@type='decimal']");
        assertEquals("//amount[@type='decimal']", amount.toString());

        SpathEngine engine = createSpathEngine("<amount type='decimal'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //amount[@type='decimal']", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testAttributeEquals2() throws XMLStreamException {
        SpathName amount = parser.parse("/amount[@type='decimal']");
        assertEquals("/amount[@type='decimal']", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //amount[@type='decimal']", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testAndAttributeEquals() throws XMLStreamException {
        SpathName amount = parser.parse("/amount[@currency='USD' and @type='decimal']");
        assertEquals("/amount[@currency='USD' and @type='decimal']", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='USD'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //amount[@type='decimal']", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testMultiPredicateMissmatch() throws XMLStreamException {
        SpathName amount = parser.parse("/amount[@currency='USD' and @type='decimal']");
        assertEquals("/amount[@currency='USD' and @type='decimal']", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='USD'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //amount[@type='decimal']", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testAttributeExists() throws XMLStreamException {
        SpathName amount = parser.parse("/amount[@type]");
        assertEquals("/amount[@type]", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='USD'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //amount[@type]", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testMultiAttributeExists() throws XMLStreamException {
        SpathName amount = parser.parse("/amount[@type and @currency]");
        assertEquals("/amount[@type and @currency]", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='USD'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //amount[@type and @currency]", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testSimpleMismatch() throws XMLStreamException {
        SpathName amount = parser.parse("/amount[ @currency = 'AUD' ] ");
        assertEquals("/amount[@currency='AUD']", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='USD'>10.25</amount>");
        engine.add(amount);
        
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeEquals() throws XMLStreamException {
        SpathName amount = parser.parse("/*[@currency='USD']");
        assertEquals("/*[@currency='USD']", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='USD'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //*[@currency='USD']", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeNotEquals() throws XMLStreamException {
        SpathName amount = parser.parse("//*[@currency!='USD']");
        assertEquals("//*[@currency!='USD']", amount.toString());
    
        SpathEngine engine = createSpathEngine("<amount type='decimal' currency='AUD'>10.25</amount>");
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //*[@currency!='USD']", engine.match(amount));
        assertEquals("10.25", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testAttributeEqualsNumber() throws XMLStreamException {
        SpathName item = parser.parse("item");
        assertEquals("//item", item.toString());
        SpathName amount = parser.parse("//item[@amount=10.25]");
        assertEquals("//item[@amount=10.25]", amount.toString());
    
        SpathEngine engine = createSpathEngine("<item amount='10.25'>Lunch</item>");
        engine.add(item);
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //item[@amount=10.25]", engine.match(amount));
        assertEquals("Lunch", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeEqualsNumber() throws XMLStreamException {
        SpathName item = parser.parse("*");
        assertEquals("//*", item.toString());
        SpathName amount = parser.parse("//*[@amount=10.25]");
        assertEquals("//*[@amount=10.25]", amount.toString());
    
        SpathEngine engine = createSpathEngine("<item amount='10.25'>Lunch</item>");
        engine.add(item);
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //item[@amount=10.25]", engine.match(amount));
        assertEquals("Lunch", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeNotEqualsNumber() throws XMLStreamException {
        SpathName item = parser.parse("*");
        assertEquals("//*", item.toString());
        SpathName amount = parser.parse("*[@amount!=10.25]");
        assertEquals("//*[@amount!=10.25]", amount.toString());
    
        SpathEngine engine = createSpathEngine("<item amount='10.00'>Lunch</item>");
        engine.add(item);
        engine.add(amount);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match //*[@amount!=10.25]", engine.match(amount));
        assertEquals("Lunch", engine.getText());
        assertFalse("Shound not matchNext()", engine.matchNext());
    }
    
    private static final String SHOES = 
            "<shoes><shoe price='20.00'/><shoe price='30.00'/><shoe price='40.00'/></shoes>"; 

    @Test
    public void testStarAttributeLessThanNumber() throws XMLStreamException {
        SpathName item = parser.parse("shoe");
        assertEquals("//shoe", item.toString());
        SpathName element = parser.parse("*[@price<30]");
        assertEquals("//*[@price<30]", element.toString());
    
        SpathEngine engine = createSpathEngine(SHOES);
        engine.add(item);
        engine.add(element);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match [@price<30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertFalse("Should not match [@price<30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertFalse("Should not match [@price<30]", engine.match(element));
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeGreaterThanNumber() throws XMLStreamException {
        SpathName item = parser.parse("shoe");
        assertEquals("//shoe", item.toString());
        SpathName element = parser.parse("*[@price>30]");
        assertEquals("//*[@price>30]", element.toString());
    
        SpathEngine engine = createSpathEngine(SHOES);
        engine.add(item);
        engine.add(element);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertFalse("Should match [@price>30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertFalse("Should not match [@price>30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should not match [@price>30]", engine.match(element));
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeLessEqualNumber() throws XMLStreamException {
        SpathName item = parser.parse("shoe");
        assertEquals("//shoe", item.toString());
        SpathName element = parser.parse("*[@price<=30]");
        assertEquals("//*[@price<=30]", element.toString());
    
        SpathEngine engine = createSpathEngine(SHOES);
        engine.add(item);
        engine.add(element);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should match [@price<=30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should not match [@price<=30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertFalse("Should not match [@price<=30]", engine.match(element));
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeGreaterEqualNumber() throws XMLStreamException {
        SpathName item = parser.parse("shoe");
        assertEquals("//shoe", item.toString());
        SpathName element = parser.parse("*[@price>=30]");
        assertEquals("//*[@price>=30]", element.toString());
    
        SpathEngine engine = createSpathEngine(SHOES);
        engine.add(item);
        engine.add(element);
        
        assertTrue("Shound matchNext()", engine.matchNext());
        assertFalse("Should match [@price>=30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should not match [@price>=30]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext());
        assertTrue("Should not match [@price>=30]", engine.match(element));
        assertFalse("Shound not matchNext()", engine.matchNext());
    }

    @Test
    public void testStarAttributeAndNumber() throws XMLStreamException {
        SpathName item = parser.parse("shoe");
        assertEquals("//shoe", item.toString());
        SpathName element = parser.parse("*[@price>=30 and @price<40]");
        assertEquals("//*[@price>=30 and @price<40]", element.toString());
    
        String shoes = "<shoes><shoe price='20.00'/><shoe price='30.00'/><shoe price='39.99'/><shoe price='40.00'/></shoes>";
        SpathEngine engine = createSpathEngine(shoes);
        engine.add(item);
        engine.add(element);
        
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='20.00'/>
        assertFalse("Should match [@price>=30 and @price<40]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='30.00'/>
        assertTrue("Should not match [@price>=30 and @price<40]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='39.99'/>
        assertTrue("Should not match [@price>=30 and @price<40]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='40.00'/>
        assertFalse("Should not match [@price>=30 and @price<40]", engine.match(element));
        assertFalse("Shound not matchNext()", engine.matchNext()); // </shoe>
    }

    @Test
    public void testStarAttributeOrNumber() throws XMLStreamException {

        SpathName item = parser.parse("shoe");
        assertEquals("//shoe", item.toString());
        SpathName element = parser.parse("*[@price<30 or @price>=40]");
        assertEquals("//*[@price<30 or @price>=40]", element.toString());
    
        String shoes = "<shoes><shoe price='20.00'/><shoe price='30.00'/><shoe price='30.01'/><shoe price='40.00'/></shoes>";
        SpathEngine engine = createSpathEngine(shoes);
        engine.add(item);
        engine.add(element);
        
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='20.00'/>
        assertTrue("Should match [[@price<30 or @price>=40]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='30.00'/>
        assertFalse("Should not match [@price<30 or @price>=40]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='30.01'/>
        assertFalse("Should not match [@price<30 or @price>=40]", engine.match(element));
        assertTrue("Shound matchNext()", engine.matchNext()); // <shoe price='40.00'/>
        assertTrue("Should not match [@price<30 or @price>=40]", engine.match(element));
        assertFalse("Shound not matchNext()", engine.matchNext()); // </shoe>
    }
}
