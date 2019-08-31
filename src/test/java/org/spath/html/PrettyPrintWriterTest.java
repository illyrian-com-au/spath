package org.spath.html;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;

import junit.framework.TestCase;

import org.junit.Test;

public class PrettyPrintWriterTest  extends TestCase {
    XMLOutputFactory xmlFactory = XMLOutputFactory.newFactory();
    
    @Test
    public void testPrettyText() throws Exception {
        StringWriter writer = new StringWriter();
        try (PrettyPrintWriter out = new PrettyPrintWriter(writer, 2)) {
            out.inc();
            out.write("Hello\nWorld");
            String output = writer.toString();
            assertEquals("Hello\nWorld", output);
        }
    }

    @Test
    public void testPrettyXml() throws Exception {
        StringWriter writer = new StringWriter();
        try (PrettyPrintWriter out = new PrettyPrintWriter(writer, 2)) {
            out.write("<data>");
            out.newLine(1);
            out.write("<greeting>");
            out.write("Hello World");
            out.write("</greeting>");
            out.newLine(-1);
            out.write("</data>");
            String output = writer.toString();
            assertEquals("<data>\n  <greeting>Hello World</greeting>\n</data>", output);
        }
    }

    @Test
    public void testStep() throws Exception {
        @SuppressWarnings("resource")
        PrettyPrintWriter out = new PrettyPrintWriter(new StringWriter());
        assertEquals(2, out.getStep());
        out.setStep(4);
        assertEquals(4, out.getStep());
        out.setStep(6);
        assertEquals(6, out.getStep());
        out.setStep(8);
        assertEquals(8, out.getStep());
        out.setStep(10);
        assertEquals(8, out.getStep());
        out.setStep(0);
        assertEquals(0, out.getStep());
        out.setStep(-2);
        assertEquals(0, out.getStep());
    }

    @Test
    public void testIncDec() throws Exception {
        @SuppressWarnings("resource")
        PrettyPrintWriter out = new PrettyPrintWriter(new StringWriter());
        assertEquals(0, out.getIndent());
        out.inc();
        assertEquals(2, out.getIndent());
        out.inc();
        assertEquals(4, out.getIndent());
        out.inc();
        assertEquals(6, out.getIndent());
        out.dec();
        assertEquals(4, out.getIndent());
        out.dec();
        assertEquals(2, out.getIndent());
        out.dec();
        assertEquals(0, out.getIndent());
        out.dec();
        assertEquals(0, out.getIndent());
    }
}
