package org.spath;

import junit.framework.TestCase;

import org.junit.Test;

public class SpathNameStarTest extends TestCase {
    SpathEvaluator<String> evaluator = new SpathEvaluatorString();
    SpathStack<String> stack = new SpathStack<>(evaluator);
    
    @Test
    public void testSimpleStar() {
        SpathName star = new SpathNameStar();
        assertEquals("/*", star.toString());

        assertFalse("Should not match " + star, stack.match(star));
        stack.push("GWML");
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
        stack.push("A");
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push("B");
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
        stack.push("A");
        stack.push("phone");
        assertEquals("[A, phone]", stack.toString());
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));

        stack.push("B");
        stack.push("phone");
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
        stack.push("A");
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push("B");
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push("phone");
        assertTrue("Should match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.pop();
        assertFalse("Should not match " + dstar, stack.match(dstar));
        stack.push("phone");
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
        stack.push("A");
        assertTrue("Should match " + star, stack.match(star));
        stack.push("B");
        assertTrue("Should match " + star, stack.match(star));
        stack.push("C");
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertTrue("Should match " + star, stack.match(star));
        stack.push("Z");
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
