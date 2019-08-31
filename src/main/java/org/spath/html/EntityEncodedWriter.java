package org.spath.html;

import java.io.IOException;
import java.io.Writer;

public class EntityEncodedWriter extends Writer {
    private final Writer delegate;
    
    public EntityEncodedWriter(Writer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i=0; i<len; i++) {
            char ch = cbuf[off+i];
            if (ch > 127) {
                delegate.write("&#");
                delegate.write(Integer.valueOf(ch));
                delegate.write(";");
            } else {
                switch (ch) {
                case '<': 
                    delegate.write("&lt;");
                    break;
                case '>': 
                    delegate.write("&gt;");
                    break;
                case '"': 
                    delegate.write("&quot;");
                    break;
                case '&': 
                    delegate.write("&amp;");
                    break;
                default:
                    delegate.write(cbuf, off+i, 1);            
                }
            }
        }
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

}
