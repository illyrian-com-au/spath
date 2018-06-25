package org.spath;


public interface SpathPredicate extends SpathMatch {
    
    public String getName();

    public SpathOperator getOperator();

    public Object getValue();
}
