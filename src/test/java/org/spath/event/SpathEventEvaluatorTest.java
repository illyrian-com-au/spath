package org.spath.event;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.SpathEvaluator;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.engine.SpathEngineImpl;
import org.spath.engine.SpathStackImpl;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathQueryBuilder;
import org.spath.test.SpathEventTestSource;

public class SpathEventEvaluatorTest extends TestCase {
    SpathEventBuilder builder = new SpathEventBuilder();
    SpathQueryBuilder spath = new SpathQueryBuilder();
    SpathEvaluator<SpathEvent> matcher = new SpathEventEvaluator();
    
    SpathEngine createEngine(SpathEvent [] list) {
        SpathEventTestSource<SpathEvent> source = new SpathEventTestSource<SpathEvent>(list);
        SpathStack<SpathEvent> stack = new SpathStackImpl<SpathEvent>(matcher);
        SpathEngine engine = new SpathEngineImpl<SpathEvent>(stack, source);
        return engine;
    }
    
    @Test
    public void testTextElement() throws Exception {
        SpathEvent [] list = {
                builder.withName("data").withText("Hello World").build(),
                null
        };
        SpathEngine engine = createEngine(list);
        SpathQuery data = spath.withName("data").build();
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
                builder.withName("data").withProperty("currency", "AUD").build(),
                null,
                builder.withName("data").withProperty("currency", "USD").build(),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathQuery data = spath.withName("data").build();
        SpathQuery usd = spath.withName("data")
                .withPredicate("currency", SpathPredicateOperator.EQ, "USD")
                .build();
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
                builder.withName("data").withProperty("amount", new BigDecimal("12.50")).build(),
                null,
                builder.withName("data").withProperty("amount", new BigDecimal("10.25")).build(),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathQuery data = spath.withName("data").build();
        SpathQuery usd = spath.withName("data")
                .withPredicate("amount", SpathPredicateOperator.LE, new BigDecimal("10.25"))
                .build();
        engine.add(usd);
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
                builder.withName("data").withProperty("amount", new BigDecimal("12.50")).build(),
                null,
                builder.withName("data").withProperty("amount", new BigDecimal("10.25")).build(),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathQuery data = spath.withName("data").build();
        SpathQuery usd = spath.withName("data")
                .withPredicate("amount", SpathPredicateOperator.LE, new BigDecimal("10.25"))
                .build();
        engine.add(usd);
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
                builder.withName("data").withProperty("paid", new Boolean(false)).build(),
                null,
                builder.withName("data").withProperty("paid", Boolean.TRUE).build(),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathQuery data = spath.withName("data").build();
        SpathQuery paid = spath.withName("data")
                .withPredicate("paid", SpathPredicateOperator.EQ, new Boolean(true))
                .build();
        engine.add(paid);
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
                builder.withName("data").withProperty("paid", "false").build(),
                null,
                builder.withName("data").withProperty("paid", "true").build(),
                null,
        };
        SpathEngine engine = createEngine(list);
        SpathQuery data = spath.withName("data").build();
        SpathQuery paid = spath.withName("data")
                .withPredicate("paid", SpathPredicateOperator.EQ, new Boolean(true))
                .build();
        engine.add(paid);
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
