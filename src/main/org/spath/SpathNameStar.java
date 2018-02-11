package org.spath;

public class SpathNameStar implements SpathName {
    private final SpathName parent;
    private final int offset;
    
    public SpathNameStar() {
        this(null, 0);
    }
    
    public SpathNameStar(SpathName parent) {
        this(parent, parent.getOffset() + 1);
    }
    
    public SpathNameStar(int offset) {
        this(null, offset);
    }
    
    public SpathNameStar(SpathName parent, int offset) {
        this.parent = parent;
        this.offset = offset;
    }
    
    public SpathName getParent() {
        return parent;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public String getValue() {
        return "*";
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> matcher, T event) {
        return matcher.match(this, event);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SpathNameStar) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        if (getParent() != null) {
            build.append(getParent().toString());
        }
        if (getOffset() < 0 && (getParent() == null || getParent().getOffset() >= 0)) {
            build.append('/');
        }
        build.append("/*");
        return build.toString();
    }
}
