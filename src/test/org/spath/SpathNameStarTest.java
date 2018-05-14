package org.spath;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.data.SpathEvent;
import org.spath.data.SpathEventEvaluator;

public class SpathNameStarTest extends TestCase {
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStack<>(evaluator);
    
    @Test
    public void testSimpleStar() {
        SpathName star = new SpathNameStar();
        assertEquals("/*", star.toString());

        assertFalse("Should not match " + star, stack.match(star));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertFalse("Should not match " + star, stack.match(star));
    }

    @Test
    public void testDoubleStar() {
        SpathName star = new SpathNameStar();
        SpathName dstar = new SpathNameStar(star);
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
        SpathName star = new SpathNameStar();
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
        SpathName star = new SpathNameStar(true);
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
        SpathName star = new SpathNameStar(true);
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
        try {
            new SpathNameStart("*");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '*' in SpathName: *", ex.getMessage());
        }
        try {
            new SpathNameStart("hello*world");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '*' in SpathName: hello*world", ex.getMessage());
        }
    }
}
