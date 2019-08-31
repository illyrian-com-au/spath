package org.spath.html;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

public class XmlEventHtmlWriter extends XmlEventWriterDelegate {
    protected final PrettyPrintWriter out;
    private boolean followsStartElement = false;
    private boolean isHtmlFragment = false;
    
    public XmlEventHtmlWriter(XMLEventWriter delegate, PrettyPrintWriter writer) {
        super(delegate);
        this.out = writer;
    }
    
    public boolean isHtmlFragment() {
        return isHtmlFragment;
    }

    public void setHtmlFragment(boolean isHtmlFragment) {
        this.isHtmlFragment = isHtmlFragment;
    }

    @Override
    public void apply(StartDocument event) throws XMLStreamException {
        if (!isHtmlFragment()) {
            startDocument();
        }
        super.apply(event);
    }
    
    @Override
    public void apply(EndDocument event) throws XMLStreamException {
        super.apply(event);
        if (!isHtmlFragment()) {
            endDocument();
        }
    }
    
    @Override
    public void apply(StartElement event) throws XMLStreamException {
        if (followsStartElement) {
            // <data>
            //     <name>
            out.newLine(0); // newline and increment
            out.print("<br/><dev id=\"indent\">");
            out.newLine(1); // newline and decrement
        } else {
            // </data>
            // <data> 
            out.newLine(0); // newline only
            out.print("<br/>");
        }
        super.apply(event);
        followsStartElement = true;
    }
    
    @Override
    public void apply(EndElement event) throws XMLStreamException {
        if (!followsStartElement) {
            //     </name>
            // </data>
            out.newLine(-1); // newline and decrement
            out.print("</dev><br/>");
            out.newLine(0); // newline and indent
        } else {
            // <name>fred</name>  // keep on same line
        }
        getDelegate().add(event);
        followsStartElement = false;
    }
    
    @Override
    public void apply(Characters event) throws XMLStreamException {
        if (!event.isWhiteSpace()) {
            getDelegate().add(event);
        }
        // Otherwise do not output whitespace between elements.
    }
    
    public void startDocument() {
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<!doctype html>");
        out.println("<html>");
        out.println("<head>");
        out.println("  <meta charset=\"utf-8\">");
        out.println("  <style type=\"text/css\">");
        out.println("    body {font-family: Monaco, monospace}");
        out.println("    #indent {");
        out.println("      position:relative;");
        out.println("      left:20px");
        out.println("    }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
    }

    public void endDocument() {
        out.println("</body>");
        out.println("</html>");
        out.close();
    };
}
