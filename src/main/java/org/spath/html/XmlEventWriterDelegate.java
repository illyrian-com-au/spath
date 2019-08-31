package org.spath.html;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public abstract class XmlEventWriterDelegate implements XMLEventWriter {
    private final XMLEventWriter delegate;
    private final XMLEventFactory factory = XMLEventFactory.newFactory();
    private final Characters NULL_CHARS = factory.createCharacters("");
    
    public XmlEventWriterDelegate(XMLEventWriter delegate) {
        this.delegate = delegate;
        if (delegate == null) {
            throw new IllegalStateException("XMLEventWriter delegate cannot be null");
        }
    }

    public XMLEventWriter getDelegate() {
        return delegate;
    }

    @Override
    public void flush() throws XMLStreamException {
        delegate.flush();
    }

    @Override
    public void close() throws XMLStreamException {
        delegate.close();
    }

    @Override
    public void add(XMLEvent event) throws XMLStreamException {
        if (event instanceof StartElement) {
            apply((StartElement)event);
        } else if (event instanceof EndElement) {
            apply((EndElement)event);
        } else if (event instanceof StartDocument) {
            apply((StartDocument)event);
        } else if (event instanceof EndDocument) {
            apply((EndDocument)event);
        } else if (event instanceof Characters) {
            apply((Characters)event);
        } else {
            delegate.add(event);
        }
    }

    public void apply(StartDocument event) throws XMLStreamException {
        delegate.add(event);
    }
    
    public void apply(EndDocument event) throws XMLStreamException {
        delegate.add(event);
    }
    
    public void apply(StartElement event) throws XMLStreamException {
        delegate.add(event);
        flushStartElement();
    }
    
    public void apply(EndElement event) throws XMLStreamException {
        delegate.add(event);
    }
    
    public void apply(Characters event) throws XMLStreamException {
        delegate.add(event);
    }
    
    @Override
    public void add(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            add(event);
        }
    }
    
    @Override
    public String getPrefix(String uri) throws XMLStreamException {
        return delegate.getPrefix(uri);
    }

    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        delegate.setPrefix(prefix, uri);
    }

    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        delegate.setDefaultNamespace(uri);
    }

    @Override
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        delegate.setNamespaceContext(context);
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return delegate.getNamespaceContext();
    }

    private void flushStartElement() throws XMLStreamException {
        delegate.add(NULL_CHARS);
    }

}
