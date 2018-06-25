package org.spath;

public interface SpathName extends SpathMatch {

    SpathName getParent();

    int getDepth();
    
    SpathType getType();
    
    SpathMatch getPredicate();
    
    // FIXME get rid of this ...
    public void add(SpathMatch matcher);
}