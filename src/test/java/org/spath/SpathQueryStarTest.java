package org.spath;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.engine.SpathStackImpl;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;
import org.spath.query.SpathAnyName;
import org.spath.query.SpathName;
import org.spath.query.SpathQueryBuilder;
import org.spath.query.SpathQueryStart;

public class SpathQueryStarTest extends TestCase {
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStackImpl<SpathEvent>(evaluator);
    SpathQueryBuilder builder = new SpathQueryBuilder();
    
    @Test
    public void testSimpleStar() {
        SpathQuery star = builder.root().withStar().build();
        assertEquals("/*", star.toString());

        assertFalse("Should not match " + star, stack.match(star));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + star, stack.match(star));
        stack.pop();
        assertFalse("Should not match " + star, stack.match(star));
    }

    @Test
    public void testDoubleStar() {
        SpathQuery dstar = builder.root().withStar().next()
                .withStar().build();
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
        SpathQuery dstar = builder.root().withStar().next()
                .withName("phone").build();
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
        SpathQuery dstar = builder
                .withStar().next()
                .withName("phone").build();
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
        SpathQuery star = builder.withStar().build();
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
        new SpathQueryStart(new SpathAnyName());
        try {
            new SpathName("!");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '!' in SpathQuery: !", ex.getMessage());
        }
        try {
            new SpathName("hello*world");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '*' in SpathQuery: hello*world", ex.getMessage());
        }
    }
}
