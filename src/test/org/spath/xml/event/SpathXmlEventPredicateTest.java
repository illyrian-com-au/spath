package org.spath.xml.event;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathName;
import org.spath.SpathNameStart;
import org.spath.SpathOperator;
import org.spath.SpathPredicate;
import org.spath.SpathPredicateString;
import org.spath.SpathStack;
import org.spath.data.SpathEvent;
import org.spath.data.SpathEventEvaluator;
import org.spath.parser.SpathParser;

import static org.spath.test.SpathEventFromString.toEvent;

public class SpathXmlEventPredicateTest extends TestCase {
    SpathParser parser = new SpathParser();
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlEventReaderFactory factory = new SpathXmlEventReaderFactory();
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStack<>(evaluator);
    
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
/*
    @Test
    public void testStarAttributeGreaterEqualNumber() {
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathOperator.GE, new BigDecimal("30"));
        element.add(attr);
        assertEquals("/*[@price>=30]", element.toString());

        assertFalse("Should not match [@price>=30]", stack.match(element));
        stack.push(toEvent("shoe(price='20')"));
        assertFalse("Should not match [@price>=30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='30')"));
        assertTrue("Should match [@price>30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='40')"));
        assertTrue("Should match [@price>30]", stack.match(element));
        stack.pop();
        assertFalse("Should not match [@price>=30]", stack.match(element));
    }

    @Test
    public void testStarAttributeAndNumber() {
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber op1 = new SpathPredicateNumber("price", SpathOperator.GE, new BigDecimal("30"));
        SpathPredicateNumber op2 = new SpathPredicateNumber("price", SpathOperator.LT, new BigDecimal("40"));
        SpathPredicateAnd expr = new SpathPredicateAnd(op1, op2);
        element.add(expr);
        assertEquals("/*[@price>=30 and @price<40]", element.toString());

        assertFalse("Should not match [@price>=30 and @price<40]", stack.match(element));
        stack.push(toEvent("shoe(price='20')"));
        assertFalse("Should not match [@price>=30 and @price<40]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='30')"));
        assertTrue("Should match [@price>=30 and @price<40]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='39.99')"));
        assertTrue("Should match [@price>=30 and @price<40]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='40')"));
        assertFalse("Should not match [@price>=30 and @price<40]", stack.match(element));
        stack.pop();
        assertFalse("Should not match [@price>=30 and @price<40]", stack.match(element));
    }

    @Test
    public void testStarAttributeOrNumber() {
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber op1 = new SpathPredicateNumber("price", SpathOperator.LT, new BigDecimal("30"));
        SpathPredicateNumber op2 = new SpathPredicateNumber("price", SpathOperator.GE, new BigDecimal("40"));
        SpathPredicateOr expr = new SpathPredicateOr(op1, op2);
        element.add(expr);
        assertEquals("/*[@price<30 or @price>=40]", element.toString());

        assertFalse("Should not match [@price<30 or @price>=40]", stack.match(element));
        stack.push(toEvent("shoe(price='20')"));
        assertTrue("Should not match [@price<30 or @price>=40]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='29.99')"));
        assertTrue("Should not match [@price<30 or @price>=40]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='30')"));
        assertFalse("Should match [@price<30 or @price>=40]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='40')"));
        assertTrue("Should not match [@price<30 or @price>=40]", stack.match(element));
        stack.pop();
        assertFalse("Should not match [@price<30 or @price>=40]", stack.match(element));
    }

    @Test
    public void testRelativeStarAttributeEquals() {
        SpathNameStar element = new SpathNameStar(true);
        SpathPredicate attr = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        element.add(attr);
        assertEquals("//*[@type='decimal']", element.toString());

        stack.push(toEvent("data"));
        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertTrue("Should match " + attr, stack.match(element));
        assertEquals("[data, amount(type='decimal')]", stack.toString());
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testInvalidCharacters() {
        try {
            new SpathNameStart("/data");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '/' in SpathName: /data", ex.getMessage());
        }
    }
    */
}
