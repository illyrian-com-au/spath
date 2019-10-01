package org.spath.query;

import org.spath.SpathEvaluator;

public class SpathAnyName extends SpathName {
    public static final String STAR = "*";
    
    public SpathAnyName() {
        super(STAR);
    }

    protected void validate(String name) {
        // Do not validate
    }

    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }
}
