package org.spath;

public interface SpathEngine {

    SpathName add(SpathName name);

    boolean hasNext();

    boolean matchAny(SpathName base) throws SpathException;

    boolean matchAny() throws SpathException;

    SpathName findMatch();

    boolean match(SpathName target);

    SpathName getLastMatched();

    String getText() throws SpathException;

}