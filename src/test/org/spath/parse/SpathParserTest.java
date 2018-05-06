package org.spath.parse;

import org.spath.SpathMatch;
import org.spath.SpathName;
import org.spath.SpathNameElement;
import org.spath.SpathNameRelative;
import org.spath.SpathNameStart;
import org.spath.SpathType;
import org.spath.parser.SpathParser;

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
        SpathName details = parser.parse("data/details");
        assertNotNull("Null result from parser", details);
        assertEquals("//data/details", details.toString());
        assertSpathNameElement(details, "details", SpathType.ELEMENT, 2);
        assertSpathNameRelative(details.getParent(), "data", 1);
    }

    public void testParserSequenceRelative() {
        SpathName result = parser.parse("//data/details");
        assertNotNull("Null result from parser", result);
        assertEquals("//data/details", result.toString());
        assertSpathNameElement(result, "details", SpathType.ELEMENT, 2);
        assertSpathNameRelative(result.getParent(), "data", 1);
    }

    public void testParserSequenceAbsolute() {
        SpathName result = parser.parse("/data/details");
        assertNotNull("Null result from parser", result);
        assertEquals("/data/details", result.toString());
        assertSpathNameElement(result, "details", SpathType.ROOT,  2);
        assertSpathNameStart(result.getParent(), "data");
    }

    public void testParserSequenceMixed() {
        SpathName result = parser.parse("/data//details/address");
        assertNotNull("Null result from parser", result);
        assertEquals("/data//details/address", result.toString());
        assertSpathNameElement(result, "address", SpathType.ELEMENT, 3);
        result = result.getParent();
        assertSpathNameRelative(result, "details", 2);
        result = result.getParent();
        assertSpathNameStart(result, "data");
    }

    private void assertSpathNameElement(SpathMatch target, String name, SpathType type, int depth) {
        assertEquals(SpathNameElement.class, target.getClass());
        SpathNameElement start = (SpathNameElement)target;
        assertEquals("Name", name, start.getName());
        assertEquals("Type", type, start.getType());
        assertEquals("Depth", depth, start.getDepth());
    }

    private void assertSpathNameStart(SpathMatch target, String name) {
        assertEquals(SpathNameStart.class, target.getClass());
        SpathNameStart start = (SpathNameStart)target;
        assertEquals("Name", name, start.getName());
        assertEquals("Type", SpathType.ROOT, start.getType());
        assertEquals("Depth", 1, start.getDepth());
    }

    private void assertSpathNameRelative(SpathMatch target, String name, int depth) {
        assertEquals(SpathNameRelative.class, target.getClass());
        SpathNameRelative element = (SpathNameRelative)target;
        assertEquals("Name", name, element.getName());
        assertEquals("Type", SpathType.RELATIVE, element.getType());
        assertEquals("Depth", depth, element.getDepth());
    }
}
