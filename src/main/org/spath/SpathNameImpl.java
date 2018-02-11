package org.spath;

public class SpathNameImpl implements SpathName {
    private final SpathName parent;
    private final String value;
    private final int offset;
    
    public SpathNameImpl(String value) {
        this(null, value, 0);
    }
    
    public SpathNameImpl(String value, int offset) {
        this(null, value, offset);
    }
    
    public SpathNameImpl(SpathName parent, String value) {
        this(parent, value, parent.getOffset() + 1);
    }
    
    public SpathNameImpl(SpathName parent, String value, int offset) {
        this.parent = parent;
        this.value = value;
        this.offset = offset;
        validate();
    }
    
    private void validate() {
        int index = -1;
        for (index=0; index<value.length(); index++) {
            char ch = value.charAt(index);
            if (!Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException("Invalid character : '" + value.charAt(index) + "' in SpathName: " + value);
            }
        }
    }
    
    @Override
    public <T> boolean match(SpathEvaluator<T> matcher, T event) {
        return matcher.match(this, event);
    }
    
    @Override
    public SpathName getParent() {
        return parent;
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public int getOffset() {
        return offset;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SpathName) {
            return toString().equals(other.toString());
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
        build.append('/');
        build.append(value);
        return build.toString();
    }
}
