package org.spath.query;

import org.spath.SpathMatch;
import org.spath.SpathQuery;


public class SpathQueryRelative extends SpathQueryElement implements SpathQuery {
    
    public SpathQueryRelative(SpathName name) {
        super(name, SpathQueryType.RELATIVE, null);
    }
    
    public SpathQueryRelative(SpathQuery parent, SpathName name) {
        super(parent, name, SpathQueryType.RELATIVE, null);
    }
    
    public SpathQueryRelative(SpathName name, SpathMatch predicate) {
        super(name, SpathQueryType.RELATIVE, predicate);
    }
    
    public SpathQueryRelative(SpathQuery parent, SpathName name, SpathMatch predicate) {
        super(parent, name, SpathQueryType.RELATIVE, predicate);
    }
}
