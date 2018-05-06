package org.spath;


public class SpathNameRelative extends SpathNameElement implements SpathName {
    
    public SpathNameRelative(String name) {
        super(name, SpathType.RELATIVE);
    }
    
    public SpathNameRelative(SpathName parent, String name) {
        super(parent, name, SpathType.RELATIVE);
    }
}
