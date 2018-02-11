package org.spath;

public interface SpathEvaluator<T> {
    boolean match(SpathNameImpl target, T event);

    boolean match(SpathNameStar target, T event);

    boolean match(SpathPredicate target, T event);
}
