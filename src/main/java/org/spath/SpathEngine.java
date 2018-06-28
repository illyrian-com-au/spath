package org.spath;


public interface SpathEngine {

    boolean matchNext();

    boolean matchNext(SpathQuery query);

    boolean matchNext(String expr);

    boolean match(SpathQuery query);
    
    boolean match(String expr);

    SpathQuery query(SpathQuery query);

    SpathQuery query(String expr);

    String getText();
}