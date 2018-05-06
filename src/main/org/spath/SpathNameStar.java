package org.spath;

public class SpathNameStar extends SpathNameElement implements SpathName {
    public static final String STAR = "*";
    
    public SpathNameStar() {
        super(STAR, SpathType.ROOT);
    }
    
    public SpathNameStar(SpathName parent) {
        super(parent, STAR);
    }
    
    public SpathNameStar(boolean relative) {
        super(STAR, relative ? SpathType.RELATIVE : SpathType.ROOT);
    }
    
    public SpathNameStar(SpathName parent, boolean relative) {
        super(parent, STAR, relative ? SpathType.RELATIVE : SpathType.ELEMENT);
    }
    
    @Override
    protected void validate(String name) {
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> matcher, T event) {
        if (matcher.match(this, event)) {
            return matchPredicate(matcher, event);
        }
        return false;
    }
    
}
