package org.spath.query;

import org.spath.SpathEvaluator;

public class SpathFunction extends SpathName {
    public SpathFunction(String name) {
        super(name);
    }

    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }

    public boolean isTerminal() {
        return true;
    }
    
    @Override
    public String toString() {
        return name + "()";
    }
}
