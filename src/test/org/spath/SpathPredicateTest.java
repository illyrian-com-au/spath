package org.spath;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.data.SpathEvent;
import org.spath.data.SpathEventEvaluator;
import static org.spath.test.SpathEventFromString.toEvent;

public class SpathPredicateTest extends TestCase {
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStack<>(evaluator);
    
    @Test
    public void testAttributeEquals() {
        SpathNameStart element = new SpathNameStart("amount");
        SpathPredicate attr = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        element.add(attr);
        assertEquals("/amount[@type='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount ( type = 'decimal' ) "));
        assertTrue("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testMultiAttributeEquals() {
        SpathNameStart element = new SpathNameStart("amount");
        SpathPredicate attr = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        element.add(attr);
        assertEquals("/amount[@type='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        SpathEvent event = toEvent("amount(currency='USD',type='decimal')");
        stack.push(event);
        //stack.push("amount[@currency='USD'][@type='decimal']");
        assertTrue("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testAndAttributeEquals() {
        SpathNameStart element = new SpathNameStart("amount");
        SpathPredicate attr1 = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        SpathPredicate attr2 = new SpathPredicateString("currency", SpathOperator.EQ, "USD");
        SpathMatch and1 = new SpathPredicateAnd(attr1, attr2);
        element.add(and1);
        assertEquals("/amount[@type='decimal' and @currency='USD']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertTrue("Should match amount[@currency='USD' and @type='decimal']", stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testMultiPredicateEquals() {
        SpathNameStart amount = new SpathNameStart("amount");
        SpathPredicate type = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        SpathPredicate currency = new SpathPredicateString("currency", SpathOperator.EQ, "USD");
        amount.add(type);
        amount.add(currency);
        assertEquals("/amount[@type='decimal' and @currency='USD']", amount.toString());

        assertFalse("Should not match " + amount, stack.match(amount));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertTrue("Should match " + type, stack.match(amount));
        stack.pop();
        assertFalse("Should not match " + amount, stack.match(amount));
    }

    @Test
    public void testMultiPredicateMissmatch() {
        SpathNameStart amount = new SpathNameStart("amount");
        SpathPredicate type = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        SpathPredicate currency = new SpathPredicateString("currency", SpathOperator.EQ, "AUD");
        amount.add(type);
        amount.add(currency);
        assertEquals("/amount[@type='decimal' and @currency='AUD']", amount.toString());

        assertFalse("Should not match " + amount, stack.match(amount));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertFalse("Should not match " + type, stack.match(amount));
        stack.pop();
        assertFalse("Should not match " + amount, stack.match(amount));
    }

    @Test
    public void testAttributeExists() {
        SpathNameStart element = new SpathNameStart("amount");
        SpathPredicate attr = new SpathPredicateString("type", null, null);
        element.add(attr);
        assertEquals("/amount[@type]", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertTrue("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testMultiAttributeExists() {
        SpathNameStart element = new SpathNameStart("amount");
        SpathPredicate attr = new SpathPredicateString("type", null, null);
        element.add(attr);
        assertEquals("/amount[@type]", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(currency='USD', type='decimal')"));
        assertTrue("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testSimpleMismatch() {
        SpathNameStart element = new SpathNameStart("amount");
        SpathPredicate attr = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        element.add(attr);
        assertEquals("/amount[@type='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='numeric')"));
        assertFalse("Should not match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeEquals() {
        SpathNameStar element = new SpathNameStar();
        SpathPredicate attr = new SpathPredicateString("type", SpathOperator.EQ, "decimal");
        element.add(attr);
        assertEquals("/*[@type='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertTrue("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeNotEquals() {
        SpathNameStar element = new SpathNameStar();
        SpathPredicate attr = new SpathPredicateString("type", SpathOperator.NE, "decimal");
        element.add(attr);
        assertEquals("/*[@type!='decimal']", element.toString());

        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='other')"));
        assertTrue("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
        stack.push(toEvent("amount(type='decimal')"));
        assertFalse("Should match " + attr, stack.match(element));
        stack.pop();
        assertFalse("Should not match " + element, stack.match(element));
    }

    @Test
    public void testStarAttributeEqualsNumber() {
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber attr = new SpathPredicateNumber("amount", SpathOperator.EQ, new BigDecimal("123.456"));
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
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber attr = new SpathPredicateNumber("amount", SpathOperator.NE, new BigDecimal("123.456"));
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
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathOperator.LT, new BigDecimal("30"));
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
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathOperator.GT, new BigDecimal("30"));
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
        SpathNameStar element = new SpathNameStar();
        SpathPredicateNumber attr = new SpathPredicateNumber("price", SpathOperator.LE, new BigDecimal("30"));
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
}
