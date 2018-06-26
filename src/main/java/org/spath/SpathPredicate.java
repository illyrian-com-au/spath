package org.spath;

import org.spath.query.SpathPredicateOperator;


public interface SpathPredicate extends SpathMatch {
    
    public String getName();

    public SpathPredicateOperator getOperator();

    public Object getValue();
}
