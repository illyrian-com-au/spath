package org.spath.xml.stream;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.spath.SpathEngine;
import org.spath.test.StringReadWriter;
import org.spath.xml.event.SpathXmlEventReaderFactory;

public class SpathXmlStreamExampleTest {
    XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
    SpathXmlEventReaderFactory spathFactory = new SpathXmlEventReaderFactory();
    
    private SpathEngine createSpathEngine(String xml) throws XMLStreamException {
        StringReader input = new StringReader(xml);
        XMLEventReader reader = xmlFactory.createXMLEventReader(input);
        SpathEngine engine = spathFactory.createEngine(reader);
        return engine;
    }

    @Test
    public void testSimpleElement() throws Exception {
        SpathEngine engine = createSpathEngine("<greeting>Hello World</greeting>");
        String text = null;
        while (engine.matchNext()) {
            if (engine.match("/greeting")) {
                text = engine.getText();
            }
        }
        assertEquals("Hello World", text);
    }

    @Test
    public void testNextDataElement() throws Exception {
        SpathEngine engine = createSpathEngine("<greeting>Hello <b>World</b> Goodbye</greeting>");
        String text = "";
        while (engine.matchNext("/greeting")) {
            if (engine.match("//b")) {
                text += engine.getText().toUpperCase();
            } else {
                text += engine.getText();
            }
        }
        assertEquals("Hello WORLD Goodbye", text);
    }
    
    @Test
    public void testNestedExample() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<data>");
        out.println("  <employee>");
        out.println("    <name>John Doe</name>");
        out.println("    <address type='Residential'>");
        out.println("      <street>1 Erehwon St</street>");
        out.println("      <suburb>Elsewhere</suburb>");
        out.println("      <state>VIC</state>");
        out.println("      <postcode>3098</postcode>");
        out.println("    </address>");
        out.println("    <address type='Business'>");
        out.println("      <street>1 Main St</street>");
        out.println("      <suburb>Elsewhere</suburb>");
        out.println("      <state>VIC</state>");
        out.println("      <postcode>3099</postcode>");
        out.println("    </address>");
        out.println("    <address type='Postal'>");
        out.println("      <street>P.O.Box 1234</street>");
        out.println("      <suburb>Elsewhere</suburb>");
        out.println("      <state>VIC</state>");
        out.println("      <postcode>3099</postcode>");
        out.println("    </address>");
        out.println("  </employee>");
        out.println("</data>");
        out.close();
        
        XMLEventReader reader = xmlFactory.createXMLEventReader(out.getLineReader());
        SpathEngine engine = spathFactory.createEngine(reader);
        
        ArrayList<Employee> list = new ArrayList<Employee>();
        while (engine.matchNext("/data")) {
            while (engine.match("employee")) {
                Employee employee = getEmployee(engine);
                list.add(employee);
            }
        }
        
        assertEquals(1, list.size());
        Employee employee = list.get(0);
        assertEquals("John Doe", employee.name);
        assertEquals("1 Erehwon St", employee.residential.street);
        assertEquals("Elsewhere", employee.residential.suburb);
        assertEquals("VIC", employee.residential.state);
        assertEquals("3098", employee.residential.postcode);
        assertEquals("1 Main St", employee.business.street);
        assertEquals("3099", employee.business.postcode);
        assertEquals("P.O.Box 1234", employee.postal.street);
        assertEquals("3099", employee.postal.postcode);
    }

    static class Address {
        String street = null;
        String suburb = null;
        String state = null;
        String postcode = null;
    }
    
    static class Employee {
        String  name = null;
        Address residential = null;
        Address business = null;
        Address postal = null;
    }
    
    Address getAddress(SpathEngine engine) {
        Address address = new Address();
        while (engine.matchNext("address")) {
            if (engine.match("street")) {
                address.street = engine.getText();
            }
            if (engine.match("suburb")) {
                address.suburb = engine.getText();
            }
            if (engine.match("state")) {
                address.state = engine.getText();
            }
            if (engine.match("postcode")) {
                address.postcode = engine.getText();
            }
        }
        return address;
    }
    
    Employee getEmployee(SpathEngine engine) {
        Employee employee = new Employee();
        while (engine.matchNext("employee")) {
            if (engine.match("name")) {
                employee.name = engine.getText();
            }
            if (engine.match("address[@type='Residential']")) {
                employee.residential = getAddress(engine);
            }
            if (engine.match("address[@type='Business']")) {
                employee.business = getAddress(engine);
            }
            if (engine.match("address[@type='Postal']")) {
                employee.postal = getAddress(engine);
            }
        }
        return employee;
    }

    @Test
    public void testNestedExampleFile() throws Exception {
        InputStream input = ClassLoader.getSystemResourceAsStream("employee.xml");
        XMLEventReader reader = xmlFactory.createXMLEventReader(input);
        SpathEngine engine = spathFactory.createEngine(reader);
        
        ArrayList<Employee> list = new ArrayList<Employee>();
        while (engine.matchNext("/data")) {
            while (engine.match("employee")) {
                Employee employee = getEmployee(engine);
                list.add(employee);
            }
        }
        
        assertEquals(1, list.size());
        Employee employee = list.get(0);
        assertEquals("John Doe", employee.name);
        assertEquals("1 Erehwon St", employee.residential.street);
        assertEquals("Elsewhere", employee.residential.suburb);
        assertEquals("VIC", employee.residential.state);
        assertEquals("3098", employee.residential.postcode);
        assertEquals("1 Main St", employee.business.street);
        assertEquals("3099", employee.business.postcode);
        assertEquals("P.O.Box 1234", employee.postal.street);
        assertEquals("3099", employee.postal.postcode);
    }
}
