package org.spath;

public interface SpathName {

    SpathName getParent();

    int getOffset();
    
    String getValue();
    
    <T> boolean match(SpathEvaluator<T> eval, T event);
}