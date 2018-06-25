package org.spath;

public class SpathPredicateAnd extends SpathBinary implements SpathMatch {

    public SpathPredicateAnd(SpathMatch left, SpathMatch right) {
        super(left, right);
    }

    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return getLeft().match(eval, event) && getRight().match(eval, event);
    }
    
    @Override
    public String toString() {
        return getLeft().toString() + " and " + getRight().toString();
    }
}
