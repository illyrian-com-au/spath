package org.spath.html;

import java.io.PrintWriter;
import java.io.Writer;

public class PrettyPrintWriter extends PrintWriter {
    private static final String SPACES = "        ";
    private static final int MAX = SPACES.length();
    private int step = 2;
    private int indent = 0;
    
    public PrettyPrintWriter(Writer writer) {
        super(writer);
    }
    
    public PrettyPrintWriter(Writer writer, int step) {
        this(writer);
        setStep(step);
    }
    
    public void setIndent(int value) {
        indent = value;
        if (indent < 0) {
            indent = 0;
        }
    }
    
    public int getIndent() {
        return indent;
    }
    
    public void setStep(int value) {
        if (value < 0) {
            step = 0;
        } else {
            step = (value <= MAX) ? value : MAX;
        }
    }
    
    public int getStep() {
        return step;
    }
    
    public void inc() {
        setIndent(indent + step);
    }

    public void dec() {
        setIndent(indent - step);
    }

    void indent() {
        for (int i=step; i<=indent; i+=step) {
            write(SPACES, 0, step);
        }
    }

    public void newLine(int change) {
        if (change < 0) {
            dec();
        } else if (change > 0) {
            inc();
        }
        println();
        indent();
    }
}
