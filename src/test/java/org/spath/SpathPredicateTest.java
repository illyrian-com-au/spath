package org.spath;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.engine.SpathStackImpl;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventBuilder;
import org.spath.event.SpathEventEvaluator;
import org.spath.query.SpathAnyName;
import org.spath.query.SpathName;
import org.spath.query.SpathQueryBuilder;
import org.spath.query.SpathQueryRelative;
import org.spath.query.SpathQueryStart;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateAnd;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateOr;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryType;

import static org.spath.test.SpathEventFromString.toEvent;

public class SpathPredicateTest extends TestCase {
    SpathQueryBuilder builder = new SpathQueryBuilder();
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStackImpl<SpathEvent>(evaluator);
    
    @Test
    public void testStringEquals() {
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("country")
                .withPredicate("name", SpathPredicateOperator.EQ, "Mexico")
                .build();
        String elementString = "Match" + element.toString();
        assertEquals("/country[@name='Mexico']", element.toString());

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("country ( name = 'Australia' ) "));
        stack.pop();
        stack.push(toEvent("country ( name = 'Mexico' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Scotland' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testStringNotEquals() {
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("country")
                .withPredicate("name", SpathPredicateOperator.NE, "Mexico")
                .build();
        String elementString = element.toString();
        assertEquals("/country[@name!='Mexico']", element.toString());

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("country ( name = 'Australia' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Mexico' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Scotland' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testStringLessThan() {
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("country")
                .withPredicate("name", SpathPredicateOperator.LT, "Mexico")
                .build();
        String elementString = element.toString();
        assertEquals("/country[@name<'Mexico']", elementString);

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("country ( name = 'Australia' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Mexico' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Scotland' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testStringLessEqual() {
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("country")
                .withPredicate("name", SpathPredicateOperator.LE, "Mexico")
                .build();
        String elementString = element.toString();
        assertEquals("/country[@name<='Mexico']", elementString);

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("country ( name = 'Australia' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Mexico' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Scotland' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testStringGreaterThan() {
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("country")
                .withPredicate("name", SpathPredicateOperator.GT, "Mexico")
                .build();
        String elementString = "Match " + element.toString();
        assertEquals("/country[@name>'Mexico']", element.toString());

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("country ( name = 'Australia' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Mexico' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Scotland' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testStringGreaterEqual() {
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("country")
                .withPredicate("name", SpathPredicateOperator.GE, "Mexico")
                .build();
        String elementString = "Match " + element.toString();
        assertEquals("/country[@name>='Mexico']", element.toString());

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("country ( name = 'Australia' ) "));
        assertFalse(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Mexico' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        stack.push(toEvent("country ( name = 'Scotland' ) "));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testAndAttributeEquals() {
        SpathPredicate attr1 = new SpathPredicateString("type", SpathPredicateOperator.EQ, "decimal");
        SpathPredicate attr2 = new SpathPredicateString("currency", SpathPredicateOperator.EQ, "USD");
        SpathMatch and1 = new SpathPredicateAnd(attr1, attr2);
        SpathQuery element = builder.withType(SpathQueryType.ROOT)
                .withName("amount")
                .withPredicate(and1)
                .build();
        String elementString = "Match " + element.toString();
        assertEquals("/amount[@type='decimal' and @currency='USD']", element.toString());

        assertFalse(elementString, stack.match(element));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertTrue(elementString, stack.match(element));
        stack.pop();
        assertFalse(elementString, stack.match(element));
    }

    @Test
    public void testMultiPredicateEquals() {
        SpathQuery amount = builder.root().withName("amount")
                .withPredicate("type", SpathPredicateOperator.EQ, "decimal")
                .withPredicate("currency", SpathPredicateOperator.EQ, "USD")
                .build();
        assertEquals("/amount[@type='decimal' and @currency='USD']", amount.toString());

        assertFalse("Should not match " + amount, stack.match(amount));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertTrue("Should match [@type='decimal']", stack.match(amount));
        stack.pop();
        assertFalse("Should not match " + amount, stack.match(amount));
    }

    @Test
    public void testMultiPredicateMissmatch() {
        SpathQuery amount = builder.root().withName("amount")
                .withPredicate("type", SpathPredicateOperator.EQ, "decimal")
                .withPredicate("currency", SpathPredicateOperator.EQ, "AUD")
                .build();
        assertEquals("/amount[@type='decimal' and @currency='AUD']", amount.toString());

        assertFalse("Should not match " + amount, stack.match(amount));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertFalse("Should not match amount(currency='USD', type='decimal')", stack.match(amount));
        stack.pop();
        assertFalse("Should not match amount(currency='USD', type='decimal')" + amount, stack.match(amount));
    }

    @Test
    public void testAttributeExists() {
        SpathQuery element = builder.root().withName("amount")
                .withPredicate("type").build();
        assertEquals("/amount[@type]", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertTrue("Should match /amount[@type]", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testMultiAttributeExists() {
        SpathQuery element = builder.root().withName("amount")
                .withPredicate("type").build();
        assertEquals("/amount[@type]", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertTrue("Should match /amount[@type]", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testSimpleMismatch() {
        SpathQuery element = builder.root().withName("amount")
                .withPredicate("type", SpathPredicateOperator.EQ, "decimal").build();
        assertEquals("/amount[@type='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='numeric')"));
        assertFalse("Should not match /amount[@type='decimal']", stack.match(element));
        stack.pop();
        assertFalse("Should not match empty stack", stack.match(element));
    }

    @Test
    public void testStarAttributeEquals() {
        SpathQuery element = builder.root().withStar()
                .withPredicate("type", SpathPredicateOperator.EQ, "decimal").build();
        assertEquals("/*[@type='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertTrue("Should match /*[@type='decimal']", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeNotEquals() {
        SpathQuery element = builder.root().withStar()
                .withPredicate("type", SpathPredicateOperator.NE, "decimal").build();
        assertEquals("/*[@type!='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='other')"));
        assertTrue("Should match /*[@type!='decimal']", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertFalse("Should match /*[@type!='decimal']", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeEqualsNumber() {
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber attr = new SpathPredicateNumber("amount", SpathPredicateOperator.EQ, new BigDecimal("123.456"));
        element.add(attr);
        assertEquals("/*[@amount=123.456]", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(amount='123.456')"));
        assertTrue("Should match [@amount=123.456]", stack.match(element));
        stack.pop();
        stack.push(toEvent("amount(amount='123.789')"));
        assertFalse("Should not match [@amount=123.456]", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeNotEqualsNumber() {
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber attr = new SpathPredicateNumber("amount", SpathPredicateOperator.NE, new BigDecimal("123.456"));
        element.add(attr);
        assertEquals("/*[@amount!=123.456]", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(amount='123.456')"));
        assertFalse("Should not match [@amount=123.456]", stack.match(element));
        stack.pop();
        stack.push(toEvent("amount(amount='123.789')"));
        assertTrue("Should match [@amount=123.456]", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeLessThanNumber() {
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathPredicateOperator.LT, new BigDecimal("30"));
        element.add(attr);
        assertEquals("/*[@price<30]", element.toString());

        assertFalse("Should not match [@price<30]", stack.match(element));
        stack.push(toEvent("shoe(price='20')"));
        assertTrue("Should match [@price<30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='30')"));
        assertFalse("Should not match [@price<30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='40')"));
        assertFalse("Should not match [@price<30]", stack.match(element));
        stack.pop();
        assertFalse("Should not match [@price<30]", stack.match(element));
    }

    @Test
    public void testStarAttributeGreaterThanNumber() {
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathPredicateOperator.GT, new BigDecimal("30"));
        element.add(attr);
        assertEquals("/*[@price>30]", element.toString());

        assertFalse("Should not match [@price>30]", stack.match(element));
        stack.push(toEvent("shoe(price='20')"));
        assertFalse("Should not match [@price>30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='30']"));
        assertFalse("Should not match [@price>30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='40')"));
        assertTrue("Should match [@price>30]", stack.match(element));
        stack.pop();
        assertFalse("Should not match [@price>30]", stack.match(element));
    }

    @Test
    public void testStarAttributeLessEqualNumber() {
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathPredicateOperator.LE, new BigDecimal("30"));
        element.add(attr);
        assertEquals("/*[@price<=30]", element.toString());

        assertFalse("Should not match [@price<=30]", stack.match(element));
        stack.push(toEvent("shoe(price='20')"));
        assertTrue("Should match [@price<=30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='30')"));
        assertTrue("Should match [@price<=30]", stack.match(element));
        stack.pop();
        stack.push(toEvent("shoe(price='40')"));
        assertFalse("Should not match [@price<=30]", stack.match(element));
        stack.pop();
        assertFalse("Should not match [@price<=30]", stack.match(element));
    }

    @Test
    public void testStarAttributeGreaterEqualNumber() {
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathPredicateOperator.GE, new BigDecimal("30"));
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
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber op1 = new SpathPredicateNumber("price", SpathPredicateOperator.GE, new BigDecimal("30"));
        SpathPredicateNumber op2 = new SpathPredicateNumber("price", SpathPredicateOperator.LT, new BigDecimal("40"));
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
        SpathQueryStart element = new SpathQueryStart();
        SpathPredicateNumber op1 = new SpathPredicateNumber("price", SpathPredicateOperator.LT, new BigDecimal("30"));
        SpathPredicateNumber op2 = new SpathPredicateNumber("price", SpathPredicateOperator.GE, new BigDecimal("40"));
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
        SpathQueryRelative element = new SpathQueryRelative(new SpathAnyName());
        SpathPredicate attr = new SpathPredicateString("type", SpathPredicateOperator.EQ, "decimal");
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
            new SpathName("/data");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '/' in SpathQuery: /data", ex.getMessage());
        }
    }
}
