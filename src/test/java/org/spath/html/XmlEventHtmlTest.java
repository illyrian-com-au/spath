package org.spath.html;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.test.StringReadWriter;

public class XmlEventHtmlTest  extends TestCase {
    XMLInputFactory inFactory = XMLInputFactory.newFactory();
    XmlEventHypertextFactory factory = new XmlEventHypertextFactory();
    
    @Test
    public void testEntityEncoding() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<?xml version=\"1.0\" ?><data><name>Fred</name></data>");
        out.close();
        XMLEventReader reader = inFactory.createXMLEventReader(out.getLineReader());

        StringWriter writer = new StringWriter();
        XmlEventHtmlWriter html = factory.createXmlEventHtmlWriter(writer);
        html.setHtmlFragment(true);
        html.add(reader);
        String output = writer.toString();
        String expected 
                = "&lt;?xml version=&quot;1.0&quot;?&gt;\n" 
                + "<br/>&lt;data&gt;\n"
                + "<br/><dev id=\"indent\">\n"
                + "  &lt;name&gt;Fred&lt;/name&gt;\n"
                + "</dev><br/>\n"
                + "&lt;/data&gt;";
        assertEquals(expected, output);
    }

    @Test
    public void testEntityEncodingOneLine() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<?xml version=\"1.0\" ?><data><name><first>Fred</first><surname>Bloggs</surname></name><role>Manager</role></data>");
        out.close();
        XMLEventReader reader = inFactory.createXMLEventReader(out.getLineReader());

        StringWriter writer = new StringWriter();
        XmlEventHtmlWriter html = factory.createXmlEventHtmlWriter(writer);
        html.setHtmlFragment(true);
        html.add(reader);
        String output = writer.toString();
        String expected 
                = "&lt;?xml version=&quot;1.0&quot;?&gt;\n" 
                + "<br/>&lt;data&gt;\n"
                + "<br/><dev id=\"indent\">\n"
                + "  &lt;name&gt;\n"
                + "  <br/><dev id=\"indent\">\n"
                + "    &lt;first&gt;Fred&lt;/first&gt;\n"
                + "    <br/>&lt;surname&gt;Bloggs&lt;/surname&gt;\n"
                + "  </dev><br/>\n"
                + "  &lt;/name&gt;\n"
                + "  <br/>&lt;role&gt;Manager&lt;/role&gt;\n"
                + "</dev><br/>\n"
                + "&lt;/data&gt;";
        assertEquals(expected, output);
    }

    @Test
    public void testEntityEncodingMultiLine() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<?xml version=\"1.0\" ?>");
        out.println("<data>");
        out.println("<name>");
        out.println("<first>Fred</first>");
        out.println("<surname>Bloggs</surname>");
        out.println("</name>");
        out.println("<role>Manager</role>");
        out.println("</data>");
        out.close();
        XMLEventReader reader = inFactory.createXMLEventReader(out.getLineReader());

        StringWriter writer = new StringWriter();
        XmlEventHtmlWriter html = factory.createXmlEventHtmlWriter(writer);
        html.setHtmlFragment(true);
        html.add(reader);
        String output = writer.toString();
        String expected 
                = "&lt;?xml version=&quot;1.0&quot;?&gt;\n" 
                + "<br/>&lt;data&gt;\n"
                + "<br/><dev id=\"indent\">\n"
                + "  &lt;name&gt;\n"
                + "  <br/><dev id=\"indent\">\n"
                + "    &lt;first&gt;Fred&lt;/first&gt;\n"
                + "    <br/>&lt;surname&gt;Bloggs&lt;/surname&gt;\n"
                + "  </dev><br/>\n"
                + "  &lt;/name&gt;\n"
                + "  <br/>&lt;role&gt;Manager&lt;/role&gt;\n"
                + "</dev><br/>\n"
                + "&lt;/data&gt;";
        assertEquals(expected, output);
    }

    @Test
    public void testFileEncoding() throws Exception {
        File infile = new File("src/test/data/Test.xml");
        System.out.println("Input file: " + infile.getAbsolutePath());
        FileReader input = new FileReader(infile);
        XMLEventReader reader = inFactory.createXMLEventReader(input);
        
        File outfile = new File("test.html");
        FileWriter output = new FileWriter(outfile);
        System.out.println("Output file: " + outfile.getAbsolutePath());
        XmlEventHtmlWriter html = factory.createXmlEventHtmlWriter(output);
        html.add(reader);
    }

}
