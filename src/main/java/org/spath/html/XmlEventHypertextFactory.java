package org.spath.html;

import java.io.Writer;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

public class XmlEventHypertextFactory {
    private final XMLOutputFactory outFactory;
    
    public XmlEventHypertextFactory() {
        outFactory = XMLOutputFactory.newFactory();
    }

    public XmlEventHypertextFactory(XMLOutputFactory factory) {
        outFactory = factory;
    }

    public XMLOutputFactory getXMLOutputFactory() {
        return outFactory;
    }
    
    XmlEventHtmlWriter createXmlEventHtmlWriter(Writer writer) throws XMLStreamException {
        PrettyPrintWriter pretty = new PrettyPrintWriter(writer);
        EntityEncodedWriter encodedWriter = new EntityEncodedWriter(writer);
        XMLEventWriter xml = outFactory.createXMLEventWriter(encodedWriter);
        XmlEventHtmlWriter html = new XmlEventHtmlWriter(xml, pretty);
        return html;
    }
    
    XmlEventHypertextWriter createXmlEventHypertextWriter(Writer writer) throws XMLStreamException {
        return this.createXmlEventHypertextWriter(writer, false);
    }
    
    XmlEventHypertextWriter createXmlEventHypertextWriter(Writer writer, boolean isHtmlFragment) throws XMLStreamException {
        PrettyPrintWriter pretty = new PrettyPrintWriter(writer);
        EntityEncodedWriter encodedWriter = new EntityEncodedWriter(writer);
        XMLEventWriter xml = outFactory.createXMLEventWriter(encodedWriter);
        XmlEventHtmlWriter html = new XmlEventHtmlWriter(xml, pretty);
        html.setHtmlFragment(isHtmlFragment);
        XmlEventHypertextWriter linker = new XmlEventHypertextWriter(html, pretty);
        return linker;
    }
}
