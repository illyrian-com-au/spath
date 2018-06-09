package org.spath;


public class SpathNameRelative extends SpathNameElement implements SpathName {
    
    public SpathNameRelative(String name) {
        super(name, SpathType.RELATIVE, null);
    }
    
    public SpathNameRelative(SpathName parent, String name) {
        super(parent, name, SpathType.RELATIVE, null);
    }
    
    public SpathNameRelative() {
        super(STAR, SpathType.RELATIVE, null);
    }
    
    public SpathNameRelative(SpathName parent) {
        super(parent, STAR, SpathType.RELATIVE, null);
    }
    
    public SpathNameRelative(String name, SpathMatch predicate) {
        super(name, SpathType.RELATIVE, predicate);
    }
    
    public SpathNameRelative(SpathName parent, String name, SpathMatch predicate) {
        super(parent, name, SpathType.RELATIVE, predicate);
    }
    
    public SpathNameRelative(SpathMatch predicate) {
        super(STAR, SpathType.RELATIVE, predicate);
    }
    
    public SpathNameRelative(SpathName parent, SpathMatch predicate) {
        super(parent, STAR, SpathType.RELATIVE, predicate);
    }
}
