package org.spath;

public interface SpathEventSource<T> {
    boolean nextEvent(SpathStack<T> engine) throws SpathException;
    
    String getText(SpathStack<T> engine) throws SpathException;
}
