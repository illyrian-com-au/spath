package org.spath;

public interface SpathEvaluator<T> {
    boolean match(SpathNameElement target, T event);

    boolean match(SpathNameRelative target, T event);

    boolean match(SpathNameStar target, T event);

    boolean match(SpathPredicateString target, T event);

    boolean match(SpathPredicateNumber target, T event);

    boolean match(SpathPredicateBoolean target, T event);
}
