package org.spath;

import junit.framework.TestCase;

import org.junit.Test;

public class SpathNameTest extends TestCase {
    SpathEvaluator<String> evaluator = new SpathEvaluator<String>() {
        @Override
        public boolean match(SpathNameImpl target, String event) {
            return target.getValue().equals(event);
        }

        @Override
        public boolean match(SpathNameStar target, String event) {
            return true;
        }
        
       @Override
        public boolean match(SpathPredicate target, String event) {
            return false;
        }
    };
    SpathStack<String> stack = new SpathStack<>(evaluator);
    
    @Test
    public void testSimpleElement() {
        SpathName gwml = new SpathNameImpl("GWML");
        assertEquals("/GWML", gwml.toString());

        assertFalse("Should not match " + gwml, stack.match(gwml));
        stack.push("GWML");
        assertTrue("Should match " + gwml, stack.match(gwml));
        stack.pop();
        assertFalse("Should not match " + gwml, stack.match(gwml));
    }

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
    public void testSimpleStack() {
        SpathName gwml = new SpathNameImpl("GWML");
        SpathName trade = new SpathNameImpl(gwml, "trade");
        assertEquals("/GWML/trade", trade.toString());
        
        assertFalse("Should not match " + gwml, stack.match(gwml));
        stack.push("GWML");
        assertTrue("Should match " + gwml, stack.match(gwml));
        assertFalse("Should not match Trade", stack.match(trade));
        stack.push("trade");
        assertTrue("Should match Trade", stack.match(trade));
        stack.pop();
        assertTrue("Should match " + gwml, stack.match(gwml));
        stack.pop();
        assertFalse("Should not match " + gwml, stack.match(gwml));
    }

    @Test
    public void testRelativeSimple() {
        SpathName topGwml = new SpathNameImpl("GWML", -1);
        SpathName topTrade = new SpathNameImpl("trade", -1);
        assertEquals("//GWML", topGwml.toString());
        assertEquals("//trade", topTrade.toString());
        
        assertFalse("Should not match " + topGwml, stack.match(topGwml));
        stack.push("GWML");
        assertTrue("Should match " + topGwml, stack.match(topGwml));
        assertFalse("Should not match " + topTrade, stack.match(topTrade));
        stack.push("trade");
        assertTrue("Should match " + topTrade, stack.match(topTrade));
        assertFalse("Should not match " + topGwml, stack.match(topGwml));
        stack.pop();
        assertTrue("Should match " + topGwml, stack.match(topGwml));
        stack.pop();
        assertFalse("Should not match " + topGwml, stack.match(topGwml));
    }

    @Test
    public void testRelativeStack() {
        SpathName topGwml = new SpathNameImpl("GWML", -2);
        SpathName topHeader = new SpathNameImpl(topGwml, "header", -1);
        SpathName topTrade = new SpathNameImpl(topGwml, "trade", -1);
        assertEquals("//GWML", topGwml.toString());
        assertEquals("//GWML/header", topHeader.toString());
        assertEquals("//GWML/trade", topTrade.toString());

        assertFalse("Should not match " + topGwml, stack.match(topGwml));
        assertFalse("Should not match " + topTrade, stack.match(topTrade));
        assertFalse("Should not match " + topHeader, stack.match(topHeader));
        stack.push("trade");
        assertFalse("Should not match " + topGwml, stack.match(topGwml));
        assertFalse("Should not match " + topTrade, stack.match(topTrade));
        assertFalse("Should not match " + topHeader, stack.match(topHeader));

        stack.push("GWML");
        assertFalse("Should not match " + topGwml, stack.match(topGwml));
        assertFalse("Should not match " + topTrade, stack.match(topTrade));
        stack.push("trade");
        assertTrue("Should match " + topTrade, stack.match(topTrade));
        assertTrue("Should match " + topGwml, stack.match(topGwml));
        stack.pop();
        stack.push("header");
        assertFalse("Should not match " + topTrade, stack.match(topTrade));
        assertTrue("Should match " + topGwml, stack.match(topGwml));
        assertTrue("Should match " + topHeader, stack.match(topHeader));
        stack.pop();
        assertFalse("Should not match " + topGwml, stack.match(topGwml));
    }

    @Test
    public void testMixedStack() {
        SpathName gwml = new SpathNameImpl("GWML");
        SpathName trade = new SpathNameImpl(gwml, "trade");
        SpathName address = new SpathNameImpl(trade, "address", -1);
        assertEquals("/GWML", gwml.toString());
        assertEquals("/GWML/trade", trade.toString());
        assertEquals("/GWML/trade//address", address.toString());

        stack.push("GWML");
        stack.push("trade");
        stack.push("swap");
        assertTrue("Should match " + trade, stack.match(trade));
        assertFalse("Should not match " + address, stack.match(address));
        stack.push("address");
        assertTrue("Should match " + trade, stack.match(trade));
        assertTrue("Should match " + address, stack.match(address));
        stack.push("home");
        assertTrue("Should match " + trade, stack.match(trade));
        assertFalse("Should not match " + address, stack.match(address));
        stack.push("address");
        assertTrue("Should match " + trade, stack.match(trade));
        assertTrue("Should match " + address, stack.match(address));
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
        SpathName dstar = new SpathNameImpl(star, "phone");
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
        SpathName star = new SpathNameStar(-2);
        SpathName dstar = new SpathNameImpl(star, "phone", -1);
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
        SpathName star = new SpathNameStar(-1);
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
            new SpathNameImpl("/gwml");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '/' in SpathName: /gwml", ex.getMessage());
        }

        try {
            new SpathNameImpl(" gwml");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : ' ' in SpathName:  gwml", ex.getMessage());
        }
    }
}
