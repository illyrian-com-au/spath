package org.spath.html;

import java.io.StringWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

import org.junit.Test;
import org.spath.test.StringReadWriter;
import org.spath.xml.SpathXmlReaderFactory;

public class EntityEncoderWriterTest  extends TestCase {
    XMLOutputFactory outFactory = XMLOutputFactory.newFactory();
    XMLInputFactory inFactory = XMLInputFactory.newFactory();
    SpathXmlReaderFactory spathFactory = new SpathXmlReaderFactory();
    
    @Test
    public void testEntityEncoding() throws Exception {
        StringWriter writer = new StringWriter();
        EntityEncodedWriter encodedWriter = new EntityEncodedWriter(writer);
        XMLStreamWriter xml = outFactory.createXMLStreamWriter(encodedWriter);
        xml.writeStartDocument();
        xml.writeStartElement("greeting");
        xml.writeCharacters("Hello World");
        xml.writeEndElement();
        xml.writeEndDocument();
        String output = writer.toString();
        assertEquals("&lt;?xml version=&quot;1.0&quot; ?&gt;&lt;greeting&gt;Hello World&lt;/greeting&gt;", output);
    }

    @Test
    public void testEntityEncodingInput() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<?xml version=\"1.0\" ?><greeting>Hello World</greeting>");
        out.close();
        XMLEventReader reader = inFactory.createXMLEventReader(out.getLineReader());

        StringWriter writer = new StringWriter();
        EntityEncodedWriter encodedWriter = new EntityEncodedWriter(writer);
        XMLEventWriter xmlWriter = outFactory.createXMLEventWriter(encodedWriter);
        xmlWriter.add(reader);
        String output = writer.toString();
        assertEquals("&lt;?xml version=&quot;1.0&quot;?&gt;&lt;greeting&gt;Hello World&lt;/greeting&gt;", output);
    }

    @Test
    public void testDivEncoding() throws Exception {
        StringReadWriter out = new StringReadWriter();
        out.println("<?xml version=\"1.0\" ?><greeting>Hello World</greeting>");
        out.close();
        XMLEventReader reader = inFactory.createXMLEventReader(out.getLineReader());

        StringWriter writer = new StringWriter();
        EntityEncodedWriter encodedWriter = new EntityEncodedWriter(writer);
        XMLEventWriter xmlWriter = outFactory.createXMLEventWriter(encodedWriter);
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                writer.append("<div>");
            }
            xmlWriter.add(event);
            if (event.isEndElement()) {
                writer.append("</div>");
            }
        }
        String output = writer.toString();
        assertEquals("&lt;?xml version=&quot;1.0&quot;?&gt;<div>&lt;greeting&gt;Hello World&lt;/greeting&gt;</div>", output);
    }

}
