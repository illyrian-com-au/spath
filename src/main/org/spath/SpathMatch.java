package org.spath;

public interface SpathMatch {

    <T> boolean match(SpathEvaluator<T> eval, T event);

}