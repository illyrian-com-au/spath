package org.spath;

public interface SpathName extends SpathMatch {

    SpathName getParent();

    int getDepth();
    
    SpathType getType();
    
    SpathMatch getPredicate();
    
    public void add(SpathMatch matcher);
}