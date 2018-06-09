package org.spath;


public class SpathNameElement implements SpathName {
    public static final String STAR = "*";

    protected final SpathName parent;
    protected final String name;
    protected final int depth;
    protected final SpathType type;
    private SpathMatch predicate = null;

    public SpathNameElement(SpathName parent) {
        this(parent, STAR, null);
    }
    
    SpathNameElement(SpathName parent, SpathMatch predicate) {
        this(parent, STAR, predicate);
    }
    
    public SpathNameElement(SpathName parent, String name) {
        this(parent, name, null);
    }
    
    SpathNameElement(SpathName parent, String name, SpathMatch predicate) {
        validate(parent);
        validate(name);
        this.parent = parent;
        this.name = name;
        this.depth = parent.getDepth() + 1;
        this.type = (parent.getType() == SpathType.ROOT) ? SpathType.ROOT : SpathType.ELEMENT;
        if (predicate != null) {
            add(predicate);
        }
    }
    
    SpathNameElement(String name, SpathType type, SpathMatch predicate) {
        validate(name);
        validate(type);
        this.parent = null;
        this.name = name;
        this.depth = 1;
        this.type = type;
        if (predicate != null) {
            add(predicate);
        }
    }
    
    SpathNameElement(SpathName parent, String name, SpathType type, SpathMatch predicate) {
        validate(parent);
        validate(name);
        validate(parent, type);
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.depth = parent.getDepth() + 1;
        if (predicate != null) {
            add(predicate);
        }
    }
    
    void validate(SpathName parent) {
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null.");
        }
    }
    
    void validate(SpathName parent, SpathType type) {
        if (type == SpathType.ROOT && parent != null && parent.getType() != SpathType.ROOT) {
            throw new IllegalArgumentException("Root can only be used at the start of a spath.");
        }
    }
    
    void validate(SpathType type) {
        if (type == SpathType.ELEMENT) {
            throw new IllegalArgumentException("Type at start of path must be ROOT or RELATIVE.");
        }
    }
    
    void validate(String name) {
        if (!isWild(name)) {
            int index = -1;
            for (index=0; index<name.length(); index++) {
                char ch = name.charAt(index);
                if (!Character.isLetterOrDigit(ch) || ch == ':') {
                    throw new IllegalArgumentException("Invalid character : '" + name.charAt(index) + "' in SpathName: " + name);
                }
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
    
    public boolean isWild(String name) {
        return STAR.equals(name);
    }

    public <T> boolean match(SpathEvaluator<T> matcher, T event) {
        if (matcher.match(this, event) || isWild(getName())) {
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