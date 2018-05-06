package org.spath;

public interface SpathEngine {

    boolean hasNext();

    boolean matchNext(SpathName base) throws SpathException;

    boolean matchNext() throws SpathException;

    boolean match(SpathName target);

    SpathMatch getLastMatched();

    SpathName add(SpathName name);

    String getText() throws SpathException;

}