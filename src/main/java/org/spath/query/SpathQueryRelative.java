package org.spath.query;

import org.spath.SpathMatch;
import org.spath.SpathQuery;


public class SpathQueryRelative extends SpathQueryElement implements SpathQuery {
    
    public SpathQueryRelative(String name) {
        super(name, SpathQueryType.RELATIVE, null);
    }
    
    public SpathQueryRelative(SpathQuery parent, String name) {
        super(parent, name, SpathQueryType.RELATIVE, null);
    }
    
    public SpathQueryRelative() {
        super(STAR, SpathQueryType.RELATIVE, null);
    }
    
    public SpathQueryRelative(SpathQuery parent) {
        super(parent, STAR, SpathQueryType.RELATIVE, null);
    }
    
    public SpathQueryRelative(String name, SpathMatch predicate) {
        super(name, SpathQueryType.RELATIVE, predicate);
    }
    
    public SpathQueryRelative(SpathQuery parent, String name, SpathMatch predicate) {
        super(parent, name, SpathQueryType.RELATIVE, predicate);
    }
    
    public SpathQueryRelative(SpathMatch predicate) {
        super(STAR, SpathQueryType.RELATIVE, predicate);
    }
    
    public SpathQueryRelative(SpathQuery parent, SpathMatch predicate) {
        super(parent, STAR, SpathQueryType.RELATIVE, predicate);
    }
}
