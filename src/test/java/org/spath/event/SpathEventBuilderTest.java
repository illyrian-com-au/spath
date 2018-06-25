package org.spath.event;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class SpathEventBuilderTest extends TestCase {
    
    @Test
    public void testBuilder() {
        SpathEventBuilder builder = new SpathEventBuilder();
        builder.withName("fred");
        builder.withProperty("currency", "AUD");
        builder.withProperty("amount", new BigDecimal("10.25"));
        builder.withProperty("paid",  false);
        assertEquals("name=fred(currency='AUD' amount=10.25 paid=false )", builder.toString());
        
        SpathEvent event = builder.build();
        assertEquals("fred", event.getName());
        List<SpathProperty> props = event.getProperties();
        assertEquals("Number of properties", 3, props.size());
        SpathProperty property1 = props.get(0);
        assertEquals("currency", property1.getName());
        assertEquals("AUD", property1.getValue());
        SpathProperty property2 = props.get(1);
        assertEquals("amount", property2.getName());
        assertEquals(new BigDecimal("10.25"), property2.getValue());
        SpathProperty property3 = props.get(2);
        assertEquals("paid", property3.getName());
        assertEquals(Boolean.FALSE, property3.getValue());
        assertEquals("fred(currency='AUD', amount=10.25, paid=false)", event.toString());
    }
}
