package org.spath;

import org.spath.query.SpathQueryException;

public interface SpathEventSource<T> {
    boolean nextEvent(SpathStack<T> engine) throws SpathQueryException;
    
    String getText(SpathStack<T> engine) throws SpathQueryException;
}
