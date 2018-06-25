package org.spath;

public interface SpathEngine {

    boolean matchNext(SpathName base) throws SpathException;

    boolean matchNext() throws SpathException;

    boolean match(SpathName target);

    SpathName add(SpathName name);

    String getText() throws SpathException;

}