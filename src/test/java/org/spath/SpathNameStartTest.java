package org.spath;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;
import org.spath.test.SpathEventTestSource;

public class SpathNameStartTest extends TestCase {
    SpathEventEvaluator evaluator = new SpathEventEvaluator();
    SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(evaluator);
    
    @Test
    public void testSimpleElement() {
        SpathName data = new SpathNameStart("data");
        assertEquals("/data", data.toString());

        assertFalse("Should not match " + data, stack.match(data));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + data, stack.match(data));
        stack.pop();
        assertFalse("Should not match " + data, stack.match(data));
    }

    @Test
    public void testSimpleStack() {
        SpathName data = new SpathNameStart("data");
        SpathName trade = new SpathNameElement(data, "trade");
        assertEquals("/data/trade", trade.toString());
        
        assertFalse("Should not match " + data, stack.match(data));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + data, stack.match(data));
        assertFalse("Should not match Trade", stack.match(trade));
        stack.push(new SpathEvent("trade"));
        assertTrue("Should match Trade", stack.match(trade));
        stack.pop();
        assertTrue("Should match " + data, stack.match(data));
        stack.pop();
        assertFalse("Should not match " + data, stack.match(data));
    }

    @Test
    public void testRelativeSimple() {
        SpathName topdata = new SpathNameRelative("data");
        SpathName topTrade = new SpathNameRelative("trade");
        assertEquals("//data", topdata.toString());
        assertEquals("//trade", topTrade.toString());
        
        assertFalse("Should not match " + topdata, stack.match(topdata));
        stack.push(new SpathEvent("data"));
        assertTrue("Should match " + topdata, stack.match(topdata));
        assertFalse("Should not match " + topTrade, stack.match(topTrade));
        stack.push(new SpathEvent("trade"));
        assertTrue("Should match " + topTrade, stack.match(topTrade));
        assertFalse("Should not match " + topdata, stack.match(topdata));
        stack.pop();
        assertTrue("Should match " + topdata, stack.match(topdata));
        stack.pop();
        assertFalse("Should not match " + topdata, stack.match(topdata));
    }

    @Test
    public void testRelativeStack() {
        SpathName topdata = new SpathNameRelative("data");
        SpathName topHeader = new SpathNameElement(topdata, "header");
        SpathName topTrade = new SpathNameElement(topdata, "trade");
        assertEquals("//data", topdata.toString());
        assertEquals("//data", 1, topdata.getDepth());
        assertEquals("//data", SpathType.RELATIVE, topdata.getType());
        assertEquals("//data/header", topHeader.toString());
        assertEquals("//data", 2, topHeader.getDepth());
        assertEquals("//data", SpathType.ELEMENT, topHeader.getType());
        assertEquals("//data/trade", topTrade.toString());
        assertEquals("//data", 2, topTrade.getDepth());
        assertEquals("//data", SpathType.ELEMENT, topTrade.getType());

        assertFalse("Should not match //data", stack.match(topdata));
        assertFalse("Should not match //data/trade", stack.match(topTrade));
        assertFalse("Should not match //data/header", stack.match(topHeader));
        stack.push(new SpathEvent("trade"));
        assertFalse("Should not match //data", stack.match(topdata));
        assertFalse("Should not match //data/trade", stack.match(topTrade));
        assertFalse("Should not match //data/header", stack.match(topHeader));

        stack.push(new SpathEvent("data"));
        assertTrue("Should match //data", stack.match(topdata));
        assertFalse("Should not match //data/trade", stack.match(topTrade));
        stack.push(new SpathEvent("trade"));
        assertTrue("Should match //data/trade", stack.match(topTrade));
        assertFalse("Should not match //data", stack.match(topdata));
        stack.pop();
        stack.push(new SpathEvent("header"));
        assertFalse("Should not match //data/trade", stack.match(topTrade));
        assertFalse("Should not match //data", stack.match(topdata));
        assertTrue("Should match //data/header", stack.match(topHeader));
        stack.pop();
        assertTrue("Should match //data", stack.match(topdata));
    }

    @Test
    public void testMixedStack() {
        SpathName data = new SpathNameStart("data");
        SpathName trade = new SpathNameElement(data, "trade");
        SpathName address = new SpathNameRelative(trade, "address");
        assertEquals("/data", data.toString());
        assertEquals("/data/trade", trade.toString());
        assertEquals("/data/trade//address", address.toString());

        stack.push(new SpathEvent("data"));
        stack.push(new SpathEvent("trade"));
        assertTrue("Should match /data/trade", stack.match(trade));
        stack.push(new SpathEvent("swap"));
        assertTrue("Should match /data/trade", stack.match(trade));
        assertFalse("Should not match /data/trade//address", stack.match(address));
        stack.push(new SpathEvent("address"));
        assertTrue("Should match /data/trade", stack.match(trade));
        assertTrue("Should match /data/trade//address", stack.match(address));
        stack.push(new SpathEvent("home"));
        assertTrue("Should match /data/trade", stack.match(trade));
        assertFalse("Should not match /data/trade//address", stack.match(address));
        stack.push(new SpathEvent("address"));
        assertTrue("Should match /data/trade", stack.match(trade));
        assertTrue("Should match /data/trade//address", stack.match(address));
    }
    
    @Test
    public void testPartialRelative() {
        SpathName address = new SpathNameRelative("address");
        SpathName street = new SpathNameElement(address, "street");
        assertEquals("//address", address.toString());
        assertEquals("//address/street", street.toString());

        stack.push(new SpathEvent("data"));
        stack.push(new SpathEvent("address"));
        assertTrue("Should match //address", stack.match(address));
        stack.push(new SpathEvent("name"));
        assertTrue("Should match //address", stack.partial(address));
        assertFalse("Should match //address", stack.match(address));
        assertFalse("Should not match //address/street", stack.match(street));
        stack.pop();
        assertTrue("Should match //address", stack.partial(address));
        assertTrue("Should match //address", stack.match(address));
        assertFalse("Should not match //address/street", stack.match(street));
        stack.push(new SpathEvent("street"));
        assertTrue("Should match //address", stack.partial(address));
        assertFalse("Should match //address", stack.match(address));
        assertTrue("Should not match //address/street", stack.match(street));
        stack.pop();
        assertTrue("Should match //address", stack.match(address));
        assertFalse("Should match //address/street", stack.match(street));
    }
    
    @Test
    public void testStreamStackEngineUsage() throws SpathException {
        SpathEvent [] events = new SpathEvent [] {
                new SpathEvent("data"), 
                new SpathEvent("header"), 
                new SpathEvent("address"), null, null, 
                new SpathEvent("trade"), 
                new SpathEvent("details"), null,
                null, null};
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathEventTestSource eventSource = new SpathEventTestSource(events);
        SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(evaluator);
        SpathEngineImpl engine = new SpathEngineImpl<SpathEvent>(stack, eventSource);
        SpathName data = engine.add(new SpathNameStart("data"));
        SpathName header = engine.add(new SpathNameElement(data, "header"));
        SpathName address = engine.add(new SpathNameElement(header, "address"));
        SpathName trade = engine.add(new SpathNameElement(data, "trade"));
        SpathName details = engine.add(new SpathNameElement(trade, "details"));
        
        boolean hasAddress = false;
        boolean hasDetails = false;
        while (engine.matchNext()) {
            if (engine.match(address)) {
                hasAddress = true;
            }
            if (engine.match(details)) {
                hasDetails = true;
            }
        }
        assertTrue("hasAddress", hasAddress);
        assertTrue("hasAddress", hasDetails);
    }
    
    private static class Address {
        String street;
        String suburb;
        String postcode;
    }

    @Test
    public void testAbsoluteAddress() throws SpathException {
        SpathEvent [] events = new SpathEvent [] {
                new SpathEvent("data"), 
                new SpathEvent("address"), 
                new SpathEvent("street").setText("1 Erehwon St"), null, 
                new SpathEvent("suburb").setText("Melbourne"), null,
                new SpathEvent("postcode").setText("3000"), null, 
                null, null};
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathEventTestSource eventSource = new SpathEventTestSource(events);
        SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(evaluator);
        SpathEngineImpl engine = new SpathEngineImpl<SpathEvent>(stack, eventSource);

        SpathName data = engine.add(new SpathNameStart("data"));
        SpathName address = engine.add(new SpathNameElement(data, "address"));
        engine.add(new SpathNameElement(address, "street"));
        engine.add(new SpathNameElement(address, "suburb"));
        engine.add(new SpathNameElement(address, "postcode"));

        Address addr = null;
        while (engine.matchNext()) {
            if (engine.match(address)) {
                addr = absoluteAddress(engine, address);
            }
        }
        assertEquals("Stack size should be zero", 0, stack.size());
        assertNotNull("Address addr should not be null", addr);
        assertEquals("1 Erehwon St", addr.street);
        assertEquals("Melbourne", addr.suburb);
        assertEquals("3000", addr.postcode);
    }
    
    private Address absoluteAddress(SpathEngineImpl engine, SpathMatch address) throws SpathException {

        Address addr = new Address();
        while (engine.matchNext("/data/address")) {
            if (engine.match("/data/address/street")) {
                addr.street = engine.getText();
            } else if (engine.match("/data/address/suburb")) {
                addr.suburb = engine.getText();
            } else if (engine.match("/data/address/postcode")) {
                addr.postcode = engine.getText();
            } 
        }
        return addr;
    }

    @Test
    public void testRelativeAddress() throws SpathException {
        SpathEvent [] events = new SpathEvent [] {
                new SpathEvent("data"), 
                new SpathEvent("address"), 
                new SpathEvent("street").setText("1 Erehwon St"), null, 
                new SpathEvent("suburb").setText("Melbourne"), null,
                new SpathEvent("postcode").setText("3000"), null, 
                null, null};
        SpathEventEvaluator evaluator = new SpathEventEvaluator();
        SpathEventTestSource eventSource = new SpathEventTestSource(events);
        SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(evaluator);
        SpathEngineImpl engine = new SpathEngineImpl<SpathEvent>(stack, eventSource);
        
        SpathName address = engine.add(new SpathNameRelative("address"));
        engine.add(new SpathNameElement(address, "street"));
        engine.add(new SpathNameElement(address, "suburb"));
        engine.add(new SpathNameElement(address, "postcode"));

        Address addr = null;
        while (engine.matchNext()) {
            if (engine.partial(address)) {
                addr = relativeAddress(engine, address);
            }
        }
        assertEquals("Stack size should be zero", 0, stack.size());
        assertNotNull("Address addr should not be null", addr);
        assertEquals("1 Erehwon St", addr.street);
        assertEquals("Melbourne", addr.suburb);
        assertEquals("3000", addr.postcode);
    }
    
    private Address relativeAddress(SpathEngineImpl engine, SpathName address) throws SpathException {
        Address addr = new Address();
        while (engine.matchNext(address)) {
            if (engine.match("//address/street")) {
                addr.street = engine.getText();
            } else if (engine.match("//address/suburb")) {
                addr.suburb = engine.getText();
            } else if (engine.match("//address/postcode")) {
                addr.postcode = engine.getText();
            } 
        }
        return addr;
    }

    @Test
    public void testInvalidCharacters() {
        try {
            new SpathNameStart("/data");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : '/' in SpathName: /data", ex.getMessage());
        }

        try {
            new SpathNameStart(" data");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid character : ' ' in SpathName:  data", ex.getMessage());
        }
    }
}
