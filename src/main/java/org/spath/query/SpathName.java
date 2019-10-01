package org.spath.query;

import org.spath.SpathEvaluator;
import org.spath.SpathMatch;

public class SpathName implements SpathMatch {
    protected final String name;
    
    public SpathName(String name) {
        validate(name);
        this.name = name;
    }
    
    protected void validate(String name) {
        int index = -1;
        for (index=0; index<name.length(); index++) {
            char ch = name.charAt(index);
            if (!Character.isLetterOrDigit(ch) || ch == ':') {
                throw new IllegalArgumentException("Invalid character : '" + name.charAt(index) + "' in SpathQuery: " + name);
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean isTerminal() {
        return false;
    }

    public String toString() {
        return name;
    }

    @Override
    public <T> boolean match(SpathEvaluator<T> eval, T event) {
        return eval.match(this, event);
    }
}
