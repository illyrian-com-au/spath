package org.spath;

import org.spath.query.SpathQueryException;

public interface SpathEventSource<T> {
    boolean nextEvent() throws SpathQueryException;
    
    String getText() throws SpathQueryException;
}
