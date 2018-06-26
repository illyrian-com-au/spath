package org.spath.parse;

import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.parser.SpathParser;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryRelative;
import org.spath.query.SpathQueryStart;
import org.spath.query.SpathQueryType;

import junit.framework.TestCase;

public class SpathParserTest extends TestCase {
    SpathParser parser = new SpathParser();

    public void testEmptyParser() {
        assertNull(parser.getInput().getLine());
        assertNull(parser.parse(null));
    }

    public void testParserNameSimple() {
        SpathMatch result = parser.parse("data");
        assertNotNull("Null result from parser", result);
        assertEquals("//data", result.toString());
        assertSpathNameRelative(result, "data", 1);
    }

    public void testParserNameRelative() {
        SpathMatch result = parser.parse("//data");
        assertNotNull("Null result from parser", result);
        assertEquals("//data", result.toString());
        assertSpathNameRelative(result, "data", 1);
    }

    public void testParserNameAbsolute() {
        SpathMatch result = parser.parse("/data");
        assertNotNull("Null result from parser", result);
        assertEquals("/data", result.toString());
        assertSpathNameStart(result, "data");
    }

    public void testParserSequenceSimple() {
        SpathQuery details = parser.parse("data/details");
        assertNotNull("Null result from parser", details);
        assertEquals("//data/details", details.toString());
        assertSpathNameElement(details, "details", SpathQueryType.ELEMENT, 2);
        assertSpathNameRelative(details.getParent(), "data", 1);
    }

    public void testParserSequenceRelative() {
        SpathQuery result = parser.parse("//data/details");
        assertNotNull("Null result from parser", result);
        assertEquals("//data/details", result.toString());
        assertSpathNameElement(result, "details", SpathQueryType.ELEMENT, 2);
        assertSpathNameRelative(result.getParent(), "data", 1);
    }

    public void testParserSequenceAbsolute() {
        SpathQuery result = parser.parse("/data/details");
        assertNotNull("Null result from parser", result);
        assertEquals("/data/details", result.toString());
        assertSpathNameElement(result, "details", SpathQueryType.ROOT,  2);
        assertSpathNameStart(result.getParent(), "data");
    }

    public void testParserSequenceMixed() {
        SpathQuery result = parser.parse("/data//details/address");
        assertNotNull("Null result from parser", result);
        assertEquals("/data//details/address", result.toString());
        assertSpathNameElement(result, "address", SpathQueryType.ELEMENT, 3);
        result = result.getParent();
        assertSpathNameRelative(result, "details", 2);
        result = result.getParent();
        assertSpathNameStart(result, "data");
    }
    
    private void assertSpathNameElement(SpathMatch target, String name, SpathQueryType type, int depth) {
        assertEquals(SpathQueryElement.class, target.getClass());
        SpathQueryElement start = (SpathQueryElement)target;
        assertEquals("Name", name, start.getName());
        assertEquals("Type", type, start.getType());
        assertEquals("Depth", depth, start.getDepth());
    }

    private void assertSpathNameStart(SpathMatch target, String name) {
        assertEquals(SpathQueryStart.class, target.getClass());
        SpathQueryStart start = (SpathQueryStart)target;
        assertEquals("Name", name, start.getName());
        assertEquals("Type", SpathQueryType.ROOT, start.getType());
        assertEquals("Depth", 1, start.getDepth());
    }

    private void assertSpathNameRelative(SpathMatch target, String name, int depth) {
        assertEquals(SpathQueryRelative.class, target.getClass());
        SpathQueryRelative element = (SpathQueryRelative)target;
        assertEquals("Name", name, element.getName());
        assertEquals("Type", SpathQueryType.RELATIVE, element.getType());
        assertEquals("Depth", depth, element.getDepth());
    }
}
