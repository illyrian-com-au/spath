package org.spath.query;

import org.spath.SpathEvaluator;
import org.spath.SpathQuery;


public class SpathQueryTerminal extends SpathQueryElement implements SpathQuery {
    public SpathQueryTerminal(SpathQuery parent, SpathName function) {
        super(parent, function, SpathQueryType.TERMINAL, null);
    }
    
    public <T> boolean match(SpathEvaluator<T> matcher, T event) {
        return matcher.match(this, event);
    }
}
