package org.spath;

import org.spath.query.SpathQueryElement;
import org.spath.query.SpathPredicateBoolean;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateString;

public interface SpathEvaluator<T> {
    boolean match(SpathQueryElement target, T event);

    boolean match(SpathPredicateString target, T event);

    boolean match(SpathPredicateNumber target, T event);

    boolean match(SpathPredicateBoolean target, T event);
}
