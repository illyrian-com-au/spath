package org.spath.test;

import java.util.List;

import org.junit.Test;
import org.spath.data.SpathEvent;
import org.spath.data.SpathProperty;

import junit.framework.TestCase;

public class SpathEventFromStringTest extends TestCase {
    @Test
    public void testSpathEvent() throws Exception {
        SpathEvent data = SpathEventFromString.toEvent("data");
        assertEquals("data", data.toString());
    }

    @Test
    public void testSpathEventProperty() throws Exception {
        SpathProperty data = SpathEventFromString.toProperty("name='value'");
        assertEquals("name", data.getName());
        assertEquals("value", data.getValue());
        assertEquals("name='value'", data.toString());
    }

    @Test
    public void testSpathEventProperties() throws Exception {
        SpathEvent data = SpathEventFromString.toEvent("exchange(currency='AUD',amount='10.25')");
        assertEquals("exchange", data.getName());
        assertNotNull("Properties should not be null", data.getProperties());
        assertEquals("properties.size()", 2, data.getProperties().size());
        List<SpathProperty> props = data.getProperties();
        SpathProperty property1 = props.get(0);
        assertEquals("currency", property1.getName());
        assertEquals("AUD", property1.getValue());
        assertEquals("currency='AUD'", property1.toString());
        SpathProperty property2 = props.get(1);
        assertEquals("amount", property2.getName());
        assertEquals("10.25", property2.getValue());
        assertEquals("amount='10.25'", property2.toString());
    }

    @Test
    public void testSpathEventList() throws Exception {
        String [] events = new String [] {"data", "address", null, "amount(currency='AUD')", null};
        SpathEvent [] data = SpathEventFromString.convert(events);
        assertNotNull("convert returned null", data);
        assertEquals("difference in length", events.length, data.length);
        assertEquals("data", data[0].getName());
        assertEquals("address", data[1].getName());
        assertEquals("amount(currency='AUD')", data[3].toString());
    }
}
