package org.spath.event;

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
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;
import org.spath.test.SpathEventTestSource;

public class SpathEventEvaluatorTest extends TestCase {
    SpathEvaluator<SpathEvent> matcher = new SpathEventEvaluator();
    
    SpathEngine createEngine(SpathEvent [] list) {
        SpathEventTestSource source = new SpathEventTestSource(list);
        SpathStack<SpathEvent> stack = new SpathStack<SpathEvent>(matcher);
        SpathEngine engine = new SpathEngineImpl<SpathEvent>(stack, source);
        return engine;
    }
    
    @Test
    public void testTextElement() throws Exception {
        SpathEvent [] list = {
                new SpathEvent("data").setText("Hello World"),
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
        SpathEvent [] list = {
                new SpathEvent("data").addProperty("currency", "AUD"),
                null,
                new SpathEvent("data").addProperty("currency", "USD"),
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
        SpathEvent [] list = {
                new SpathEvent("data").addProperty("amount", new BigDecimal("12.50")),
                null,
                new SpathEvent("data").addProperty("amount", new BigDecimal("10.25")),
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
        SpathEvent [] list = {
                new SpathEvent("data").addProperty("amount", "12.50"),
                null,
                new SpathEvent("data").addProperty("amount", "10.25"),
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
        SpathEvent [] list = {
                new SpathEvent("data").addProperty("paid", new Boolean(false)),
                null,
                new SpathEvent("data").addProperty("paid", new Boolean(true)),
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
        SpathEvent [] list = {
                new SpathEvent("data").addProperty("paid", "false"),
                null,
                new SpathEvent("data").addProperty("paid", "true"),
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
