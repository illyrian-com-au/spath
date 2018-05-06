package org.spath.impl;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathEngineImpl;
import org.spath.SpathEvaluator;
import org.spath.SpathName;
import org.spath.SpathNameStart;
import org.spath.SpathOperator;
import org.spath.SpathPredicateBoolean;
import org.spath.SpathPredicateNumber;
import org.spath.SpathPredicateString;
import org.spath.SpathStack;

public class SpathEvaluatorTest extends TestCase {
    SpathEvaluator<SpathElement> matcher = new SpathElementEvaluator();
    
    SpathEngine createEngine(SpathElement [] list) {
        SpathElementEventSource source = new SpathElementEventSource(list);
        SpathStack<SpathElement> stack = new SpathStack<SpathElement>(matcher);
        SpathEngine engine = new SpathEngineImpl<SpathElement>(stack, source);
        return engine;
    }
    
    @Test
    public void testTextElement() throws Exception {
        SpathElement [] list = {
                new SpathElement("data").setText("Hello World"),
                null
        };
        SpathEngine engine = createEngine(list);
        SpathName data = new SpathNameStart("data");
        engine.add(data);
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertEquals("Hello World", engine.getText());
        // Check that getText does not change the state
        assertEquals("Hello World", engine.getText());
        assertFalse("End of input", engine.matchNext());

    }
    
    @Test
    public void testPredicateStringEquals() throws Exception {
        SpathElement [] list = {
                new SpathElement("data").addProperty("currency", "AUD"),
                null,
                new SpathElement("data").addProperty("currency", "USD"),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathNameStart data = new SpathNameStart("data");
        SpathNameStart usd = new SpathNameStart("data");
        usd.add(new SpathPredicateString("currency", SpathOperator.EQ, "USD"));
        engine.add(data);
        // /data(currency="AUD") 
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertFalse("match(usd)", engine.match(usd));
        // /data(currency="USD")
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertTrue("match(usd)", engine.match(usd));
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testPredicateNumberEquals() throws Exception {
        SpathElement [] list = {
                new SpathElement("data").addProperty("amount", new BigDecimal("12.50")),
                null,
                new SpathElement("data").addProperty("amount", new BigDecimal("10.25")),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathNameStart data = new SpathNameStart("data");
        SpathNameStart usd = new SpathNameStart("data");
        usd.add(new SpathPredicateNumber("amount", SpathOperator.LE, new BigDecimal("10.25")));
        engine.add(data);
        // /data(amount=12.50) 
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertFalse("match(usd)", engine.match(usd));
        // /data(amount=10.25)
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertTrue("match(usd)", engine.match(usd));
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testPredicateNumberEqualsString() throws Exception {
        SpathElement [] list = {
                new SpathElement("data").addProperty("amount", "12.50"),
                null,
                new SpathElement("data").addProperty("amount", "10.25"),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathNameStart data = new SpathNameStart("data");
        SpathNameStart usd = new SpathNameStart("data");
        usd.add(new SpathPredicateNumber("amount", SpathOperator.LE, new BigDecimal("10.25")));
        engine.add(data);
        // /data(amount=12.50) 
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertFalse("match(usd)", engine.match(usd));
        // /data(amount=10.25)
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertTrue("match(usd)", engine.match(usd));
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testPredicateBooleanEquals() throws Exception {
        SpathElement [] list = {
                new SpathElement("data").addProperty("paid", new Boolean(false)),
                null,
                new SpathElement("data").addProperty("paid", new Boolean(true)),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathNameStart data = new SpathNameStart("data");
        SpathNameStart paid = new SpathNameStart("data");
        paid.add(new SpathPredicateBoolean("paid", SpathOperator.EQ, new Boolean(true)));
        engine.add(data);
        // /data(paid=true) 
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertFalse("match(paid)", engine.match(paid));
        // /data(paid=false)
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertTrue("match(paid)", engine.match(paid));
        assertFalse("End of input", engine.matchNext());
    }

    @Test
    public void testPredicateBooleanEqualsString() throws Exception {
        SpathElement [] list = {
                new SpathElement("data").addProperty("paid", "false"),
                null,
                new SpathElement("data").addProperty("paid", "true"),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathNameStart data = new SpathNameStart("data");
        SpathNameStart paid = new SpathNameStart("data");
        paid.add(new SpathPredicateBoolean("paid", SpathOperator.EQ, new Boolean(true)));
        engine.add(data);
        // /data(paid=true) 
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertFalse("match(paid)", engine.match(paid));
        // /data(paid=false)
        assertTrue("matchNext()", engine.matchNext());
        assertTrue("match(data)", engine.match(data));
        assertTrue("match(paid)", engine.match(paid));
        assertFalse("End of input", engine.matchNext());
    }
}
