package org.spath;

import org.spath.query.SpathFunction;
import org.spath.query.SpathName;
import org.spath.query.SpathPredicateBoolean;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryElement;

public interface SpathEvaluator<T> {
    boolean match(SpathQueryElement target, T event);

    boolean match(SpathName target, T event);

    boolean match(SpathFunction target, T event);

    boolean match(SpathPredicateString target, T event);

    boolean match(SpathPredicateNumber target, T event);

    boolean match(SpathPredicateBoolean target, T event);
}
