package org.spath;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;

public class SpathNameStarTest extends TestCase {
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(evaluator);
    
    @Test
    public void testSimpleStar() {
        SpathName star = new SpathNameStart();
        assertEquals("/*", star.toString());

        assertFalse("Should not match " + star, stack.match(star));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertFalse("Should not match " + star, stack.match(star));
    }

    @Test
    public void testDoubleStar() {
        SpathName star = new SpathNameStart();
        SpathName dstar = new SpathNameElement(star);
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
        SpathName star = new SpathNameStart();
        SpathName dstar = new SpathNameElement(star, "phone");
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
        SpathName star = new SpathNameRelative();
        SpathName dstar = new SpathNameElement(star, "phone");
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
        SpathName star = new SpathNameRelative();
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
        new SpathNameStart(SpathNameElement.STAR);
        try {
            new SpathNameStart("!");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '!' in SpathName: !", ex.getMessage());
        }
        try {
            new SpathNameStart("hello*world");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '*' in SpathName: hello*world", ex.getMessage());
        }
    }
}
