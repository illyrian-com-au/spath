package org.spath.query;

import org.spath.SpathEvaluator;

public class SpathQualifiedName extends SpathName {
    protected final String qualifier;
    
    public SpathQualifiedName(String qualifier, String name) {
        super(name);
        validate(qualifier);
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return qualifier;
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }

    @Override
    public String toString() {
        return qualifier + ':' + getName();
    }
}
