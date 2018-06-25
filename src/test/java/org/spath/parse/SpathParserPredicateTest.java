package org.spath.parse;

import junit.framework.TestCase;

import org.spath.SpathName;
import org.spath.parser.SpathParser;

public class SpathParserPredicateTest extends TestCase {
    SpathParser parser = new SpathParser();

    public void testPredicateAttributeName() {
        SpathName result = parser.parse("data[@currency]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency]", result.toString());
    }

    public void testPredicateAttributeEqualsTrue() {
        SpathName result = parser.parse("data[@currency=true]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency=true]", result.toString());
    }

    public void testPredicateAttributeNotEqualsTrue() {
        SpathName result = parser.parse("data[@currency!=TRUE]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency!=true]", result.toString());
    }

    public void testPredicateAttributeEqualsFalse() {
        SpathName result = parser.parse("data[@currency=False]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency=false]", result.toString());
    }

    public void testPredicateAttributeNotEqualsFalse() {
        SpathName result = parser.parse("data[@currency!=false]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency!=false]", result.toString());
    }

    public void testPredicateAttributeEqualsString() {
        SpathName result = parser.parse("data[@currency='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency='AUD']", result.toString());
    }

    public void testPredicateAttributeNotEqualsString() {
        SpathName result = parser.parse("data[@currency!='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency!='AUD']", result.toString());
    }

    public void testPredicateAttributeLessThanString() {
        SpathName result = parser.parse("data[@currency<'AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency<'AUD']", result.toString());
    }

    public void testPredicateAttributeLessEqualsString() {
        SpathName result = parser.parse("data[@currency<='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency<='AUD']", result.toString());
    }

    public void testPredicateAttributeGreaterThanString() {
        SpathName result = parser.parse("data[@currency>'AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency>'AUD']", result.toString());
    }

    public void testPredicateAttributeGreaterEqualsString() {
        SpathName result = parser.parse("data[@currency>='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency>='AUD']", result.toString());
    }

    public void testPredicateAttributeEqualsNumber() {
        SpathName result = parser.parse("data[@amount=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount=10.25]", result.toString());
    }

    public void testPredicateAttributeNotEqualsNumber() {
        SpathName result = parser.parse("data[@amount!=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount!=10.25]", result.toString());
    }

    public void testPredicateAttributeLessThanNumber() {
        SpathName result = parser.parse("data[@amount<10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount<10.25]", result.toString());
    }

    public void testPredicateAttributeLessEqualNumber() {
        SpathName result = parser.parse("data[@amount<=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount<=10.25]", result.toString());
    }

    public void testPredicateAttributeGreaterThanNumber() {
        SpathName result = parser.parse("data[@amount>10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount>10.25]", result.toString());
    }

    public void testPredicateAttributeGreaterEqualNumber() {
        SpathName result = parser.parse("data[@amount>=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount>=10.25]", result.toString());
    }
}
