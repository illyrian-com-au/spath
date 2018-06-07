package org.spath;

import java.math.BigDecimal;

public class SpathNameBuilder {
    private SpathName parent = null;
    private String name = null;
    private SpathType type = SpathType.RELATIVE;
    private SpathMatch predicate = null;
    
    public SpathNameBuilder(){
    }
    
    public SpathNameBuilder(SpathName parent){
        setParent(parent);
    }
    
    /*
     * setParent is private because it must be set first to determine type.
     */
    private void setParent(SpathName parent) {
        if (parent.getType() == SpathType.ROOT) {
            this.type = SpathType.ROOT;
        } else {
            this.type = SpathType.ELEMENT;
        }
        this.parent = parent;
    }
    
    public SpathNameBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public SpathNameBuilder withStar() {
        this.name = SpathNameElement.STAR;
        return this;
    }
    
    public SpathNameBuilder withType(SpathType type) {
        this.type = type;
        return this;
    }
    
    public SpathNameBuilder withPredicate(SpathMatch predicate) {
        this.predicate = predicate;
        return this;
    }
    
    public SpathNameBuilder withPredicate(String name) {
        this.predicate = new SpathPredicateString(name);
        return this;
    }
    
    public SpathNameBuilder withPredicate(String name, SpathOperator operator, String value) {
        this.predicate = new SpathPredicateString(name, operator, value);
        return this;
    }
    
    public SpathNameBuilder withPredicate(String name, SpathOperator operator, BigDecimal value) {
        this.predicate = new SpathPredicateNumber(name, operator, value);
        return this;
    }
    
    public SpathNameBuilder withPredicate(String name, SpathOperator operator, Boolean value) {
        this.predicate = new SpathPredicateBoolean(name, operator, value);
        return this;
    }
    
    public SpathNameBuilder next() {
        parent = build();
        return this;
    }
    
    void reset() {
        parent = null;
        name = null;
        type = SpathType.ELEMENT;
        predicate = null;
    }
    
    public SpathName build() {
        SpathNameElement element = null;
        if (parent == null) { 
            if (SpathType.RELATIVE == type) {
                element = new SpathNameRelative(name);
            } else if (SpathType.ROOT == type) {
                element = new SpathNameStart(name);
            } else {
                throw new IllegalArgumentException("Type at start of path must be ROOT or RELATIVE.");
            }
        } else if (SpathType.RELATIVE == type) {
            element = new SpathNameRelative(parent, name);
        } else {
            element = new SpathNameElement(parent, name);
        }
        element.add(predicate);
        reset();
        setParent(element);
        return element;
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        if (parent != null) {
            build.append("parent=").append(parent.toString()).append(", ");
        }
        build.append("name=").append(name).append(", ");
        build.append("type=").append(type).append(", ");
        if (predicate != null) {
            build.append("predicate=[");
            build.append(predicate.toString());
            build.append(']');
        }
        return build.toString();
    }
}
