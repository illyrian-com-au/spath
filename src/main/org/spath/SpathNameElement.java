package org.spath;


public class SpathNameElement implements SpathName {
    protected final SpathName parent;
    protected final String name;
    protected final int depth;
    protected final SpathType type;
    private SpathMatch predicate = null;

    public SpathNameElement(SpathName parent, String name) {
        validate(parent);
        validate(name);
        this.parent = parent;
        this.name = name;
        this.depth = parent.getDepth() + 1;
        this.type = (parent.getType() == SpathType.ROOT) ? SpathType.ROOT : SpathType.ELEMENT;
    }
    
    protected SpathNameElement(String name, SpathType type) {
        validate(name);
        this.parent = null;
        this.name = name;
        this.depth = 1;
        this.type = type;
    }
    
    protected SpathNameElement(SpathName parent, String name, SpathType type) {
        validate(parent);
        validate(name);
        validate(type);
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.depth = parent.getDepth() + 1;
    }
    
    private void validate(SpathName parent) {
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null.");
        }
    }
    
    private void validate(SpathType type) {
        if (type == SpathType.ROOT) {
            throw new IllegalArgumentException("Root can only be used at the start of a spath.");
        }
    }
    
    protected void validate(String name) {
        int index = -1;
        for (index=0; index<name.length(); index++) {
            char ch = name.charAt(index);
            if (!Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException("Invalid character : '" + name.charAt(index) + "' in SpathName: " + name);
            }
        }
    }

    public void add(SpathMatch matcher) {
        if (predicate == null) {
            predicate = matcher;
        } else {
            predicate = new SpathPredicateAnd(predicate, matcher);
        }
    }

    public <T> boolean match(SpathEvaluator<T> matcher, T event) {
        if (matcher.match(this, event)) {
            return matchPredicate(matcher, event);
        }
        return false;
    }

    public <T> boolean matchPredicate(SpathEvaluator<T> matcher, T event) {
        if (predicate != null) {
            return predicate.match(matcher, event);
        }
        return true;
    }

    public SpathName getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public int getDepth() {
        return depth;
    }
    
    public SpathType getType() {
        return type;
    }
    
    public SpathMatch getPredicate() {
        return predicate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (getClass().isInstance(other)) {
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
        if (getType() == SpathType.RELATIVE) {
            build.append('/');
        }
        build.append('/');
        build.append(name);
        if (predicate != null) {
            build.append('[');
            build.append(predicate.toString());
            build.append(']');
        }
        return build.toString();
    }
}