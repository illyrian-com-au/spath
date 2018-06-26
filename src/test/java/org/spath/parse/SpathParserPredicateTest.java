package org.spath.parse;

import junit.framework.TestCase;

import org.spath.SpathQuery;
import org.spath.parser.SpathParser;

public class SpathParserPredicateTest extends TestCase {
    SpathParser parser = new SpathParser();

    public void testPredicateAttributeName() {
        SpathQuery result = parser.parse("data[@currency]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency]", result.toString());
    }

    public void testPredicateAttributeEqualsTrue() {
        SpathQuery result = parser.parse("data[@currency=true]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency=true]", result.toString());
    }

    public void testPredicateAttributeNotEqualsTrue() {
        SpathQuery result = parser.parse("data[@currency!=TRUE]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency!=true]", result.toString());
    }

    public void testPredicateAttributeEqualsFalse() {
        SpathQuery result = parser.parse("data[@currency=False]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency=false]", result.toString());
    }

    public void testPredicateAttributeNotEqualsFalse() {
        SpathQuery result = parser.parse("data[@currency!=false]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency!=false]", result.toString());
    }

    public void testPredicateAttributeEqualsString() {
        SpathQuery result = parser.parse("data[@currency='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency='AUD']", result.toString());
    }

    public void testPredicateAttributeNotEqualsString() {
        SpathQuery result = parser.parse("data[@currency!='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency!='AUD']", result.toString());
    }

    public void testPredicateAttributeLessThanString() {
        SpathQuery result = parser.parse("data[@currency<'AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency<'AUD']", result.toString());
    }

    public void testPredicateAttributeLessEqualsString() {
        SpathQuery result = parser.parse("data[@currency<='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency<='AUD']", result.toString());
    }

    public void testPredicateAttributeGreaterThanString() {
        SpathQuery result = parser.parse("data[@currency>'AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency>'AUD']", result.toString());
    }

    public void testPredicateAttributeGreaterEqualsString() {
        SpathQuery result = parser.parse("data[@currency>='AUD']");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@currency>='AUD']", result.toString());
    }

    public void testPredicateAttributeEqualsNumber() {
        SpathQuery result = parser.parse("data[@amount=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount=10.25]", result.toString());
    }

    public void testPredicateAttributeNotEqualsNumber() {
        SpathQuery result = parser.parse("data[@amount!=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount!=10.25]", result.toString());
    }

    public void testPredicateAttributeLessThanNumber() {
        SpathQuery result = parser.parse("data[@amount<10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount<10.25]", result.toString());
    }

    public void testPredicateAttributeLessEqualNumber() {
        SpathQuery result = parser.parse("data[@amount<=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount<=10.25]", result.toString());
    }

    public void testPredicateAttributeGreaterThanNumber() {
        SpathQuery result = parser.parse("data[@amount>10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount>10.25]", result.toString());
    }

    public void testPredicateAttributeGreaterEqualNumber() {
        SpathQuery result = parser.parse("data[@amount>=10.25]");
        assertNotNull("Null result from parser", result);
        assertNotNull("Null predicate", result.getPredicate());
        assertEquals("//data[@amount>=10.25]", result.toString());
    }
}
