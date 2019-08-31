package org.spath;

public interface SpathEngine {

    /* Find a match amongst the queries that have been added */
    public SpathMatch findMatch();

    /** Match the given query against the current event */
    boolean match(SpathQuery query);
    
    /** Match the given Spath expression against the current event */
    boolean match(String expr);

    /** Add to set of queries that the engine matches */
    SpathQuery add(SpathQuery query);

    /** Add the  Spath expression to set of queries that the engine matches */
    SpathQuery add(String expr);
}