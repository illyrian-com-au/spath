package org.spath.event;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.event.SpathEvent;
import org.spath.event.SpathEventEvaluator;
import org.spath.query.SpathName;
import org.spath.query.SpathQueryStart;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateBoolean;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateString;

public class SpathEventTest extends TestCase {
    SpathEventBuilder builder = new SpathEventBuilder();

    @Test
    public void testSpathEvent() throws Exception {
        SpathEvent data = builder.withName("data").build();
        assertEquals("data", data.toString());
    }

    @Test
    public void testSpathEventProperties() throws Exception {
        SpathEvent data = builder
                .withName("data")
                .withProperty("currency", "AUD")
                .withProperty("amount", "10.25")
                .build();
        assertEquals("data(currency='AUD', amount='10.25')", data.toString());
    }
    
    @Test
    public void testSpathEventEvaluator() throws Exception {
        SpathEvent event1 = builder
                .withName("data")
                .withProperty("currency", "AUD")
                .withProperty("amount", "10.25")
                .withProperty("paid", "True")
                .build();
        assertEquals("data(currency='AUD', amount='10.25', paid='True')", event1.toString());
        SpathEvent event2 = builder
                .withName("data")
                .withProperty("date", "2018-03-21")
                .build();
        assertEquals("data(date='2018-03-21')", event2.toString());

        SpathEventEvaluator eval = new SpathEventEvaluator();
        SpathQueryStart spath = new SpathQueryStart(new SpathName("data"));
        assertTrue("match(\"data\")", eval.match(spath, event1));
        assertTrue("match(\"data\")", eval.match(spath, event2));
        
        SpathPredicateString currency = new SpathPredicateString("currency", SpathPredicateOperator.EQ, "AUD");
        assertTrue("match: data(currency='AUD')", eval.match(currency, event1));
        assertFalse("not match: data(currency='AUD')", eval.match(currency, event2));
        
        SpathPredicateNumber amount = new SpathPredicateNumber("amount", SpathPredicateOperator.GT, new BigDecimal("10.00"));
        assertTrue("match: data(amount>10.00)", eval.match(amount, event1));
        assertFalse("not match: data(amount>10.00))", eval.match(amount, event2));
        
        SpathPredicateBoolean paid = new SpathPredicateBoolean("paid", SpathPredicateOperator.EQ, Boolean.TRUE);
        assertTrue("match: data(paid=true)", eval.match(paid, event1));
        assertFalse("not match: data(paid=true)", eval.match(paid, event2));
        
        SpathPredicateString date = new SpathPredicateString("date", SpathPredicateOperator.EQ, "2018-03-21");
        assertFalse("not match: data(date='2018-03-21')", eval.match(date, event1));
        assertTrue("match: data(date='2018-03-21')", eval.match(date, event2));
    }
    
}
