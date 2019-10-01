package org.spath.html;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.StartElement;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.SpathStack;
import org.spath.engine.SpathStackImpl;
import org.spath.test.StringReadWriter;
import org.spath.xml.SpathXmlEventEvaluator;
import org.spath.xml.SpathXmlReaderFactory;

public class XmlEventHypertextTest  extends TestCase {
    XMLOutputFactory outFactory = XMLOutputFactory.newFactory();
    XMLInputFactory inFactory = XMLInputFactory.newFactory();
    SpathXmlReaderFactory spathFactory = new SpathXmlReaderFactory();
    XmlEventHypertextFactory factory = new XmlEventHypertextFactory();
    
    @Test
    public void testEntityEncoding() throws Exception {
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
        XmlEventHypertextWriter linker = factory.createXmlEventHypertextWriter(writer, true);
        linker.addTarget("ROLE", "//role");
        linker.addTarget("SURNAME", "//surname");
        linker.add(reader);
        String output = writer.toString();
        String expected 
                = "&lt;?xml version=&quot;1.0&quot;?&gt;\n" 
                + "<br/>&lt;data&gt;\n"
                + "<br/><dev id=\"indent\">\n"
                + "  &lt;name&gt;\n"
                + "  <br/><dev id=\"indent\">\n"
                + "    &lt;first&gt;Fred&lt;/first&gt;\n"
                + "    <br/>&lt;surname&gt;<a href=\"#SURNAME\">Bloggs</a>&lt;/surname&gt;\n"
                + "  </dev><br/>\n"
                + "  &lt;/name&gt;\n"
                + "  <br/>&lt;role&gt;<a href=\"#ROLE\">Manager</a>&lt;/role&gt;\n"
                + "</dev><br/>\n"
                + "&lt;/data&gt;";
        assertEquals(expected, output);
    }

    @Test
    public void testFileHypertext() throws Exception {
        File infile = new File("src/test/data/Test.xml");
        System.out.println("Input file: " + infile.getAbsolutePath());
        FileReader input = new FileReader(infile);
        XMLEventReader reader = inFactory.createXMLEventReader(input);
        
        File outfile = new File("target/hypertext.html");
        FileWriter output = new FileWriter(outfile);
        System.out.println("Output file: " + outfile.getAbsolutePath());
        XmlEventHypertextWriter linker = factory.createXmlEventHypertextWriter(output);
        linker.addTarget("ROLE", "//role");
        linker.addTarget("SURNAME", "//surname");
        linker.add(reader);
        reader.close();
    }
}
