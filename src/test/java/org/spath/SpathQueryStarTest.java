package org.spath;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.engine.SpathStackImpl;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;
import org.spath.query.SpathQueryElement;
import org.spath.query.SpathQueryRelative;
import org.spath.query.SpathQueryStart;

public class SpathQueryStarTest extends TestCase {
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStackImpl<SpathEvent>(evaluator);
    
    @Test
    public void testSimpleStar() {
        SpathQuery star = new SpathQueryStart();
        assertEquals("/*", star.toString());

        assertFalse("Should not match " + star, stack.match(star));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertFalse("Should not match " + star, stack.match(star));
    }

    @Test
    public void testDoubleStar() {
        SpathQuery star = new SpathQueryStart();
        SpathQuery dstar = new SpathQueryElement(star);
        assertEquals("/*/*", dstar.toString());

        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("A"));
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("B"));
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
    }

    @Test
    public void testStarPhone() {
        SpathQuery star = new SpathQueryStart();
        SpathQuery dstar = new SpathQueryElement(star, "phone");
        assertEquals("/*/phone", dstar.toString());

        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("A"));
        stack.push(new SpathEvent("phone"));
        assertEquals("[A, phone]", stack.toString());
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));

        stack.push(new SpathEvent("B"));
        stack.push(new SpathEvent("phone"));
        assertEquals("[B, phone]", stack.toString());
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
    }

    @Test
    public void testAnyStarPhone() {
        SpathQuery star = new SpathQueryRelative();
        SpathQuery dstar = new SpathQueryElement(star, "phone");
        assertEquals("//*/phone", dstar.toString());

        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("A"));
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("B"));
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("phone"));
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push(new SpathEvent("phone"));
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
    }

    @Test
    public void testAnyStar() {
        SpathQuery star = new SpathQueryRelative();
        assertEquals("//*", star.toString());

        assertFalse("Should not match " + star, stack.match(star));
        stack.push(new SpathEvent("A"));
        assertTrue("Should match " + star, stack.match(star));
        stack.push(new SpathEvent("B"));
        assertTrue("Should match " + star, stack.match(star));
        stack.push(new SpathEvent("C"));
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertTrue("Should match " + star, stack.match(star));
        stack.push(new SpathEvent("Z"));
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertFalse("Should not match " + star, stack.match(star));
    }

    @Test
    public void testInvalidCharacters() {
        new SpathQueryStart(SpathQueryElement.STAR);
        try {
            new SpathQueryStart("!");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '!' in SpathQuery: !", ex.getMessage());
        }
        try {
            new SpathQueryStart("hello*world");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '*' in SpathQuery: hello*world", ex.getMessage());
        }
    }
}
