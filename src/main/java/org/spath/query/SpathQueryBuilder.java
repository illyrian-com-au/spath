package org.spath.query;

import java.math.BigDecimal;

import org.spath.SpathMatch;
import org.spath.SpathQuery;

public class SpathQueryBuilder {
    private SpathQuery parent = null;
    private String name = null;
    private SpathQueryType type = SpathQueryType.RELATIVE;
    private SpathMatch predicate = null;
    
    public SpathQueryBuilder(){
    }
    
    public SpathQueryBuilder(SpathQuery parent){
        setParent(parent);
    }
    
    /*
     * setParent is private because it should be used either from the constructor or next method.
     */
    private void setParent(SpathQuery parent) {
        if (parent.getType() == SpathQueryType.ROOT) {
            this.type = SpathQueryType.ROOT;
        } else {
            this.type = SpathQueryType.ELEMENT;
        }
        this.parent = parent;
    }
    
    public SpathQueryBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public SpathQueryBuilder withStar() {
        this.name = SpathQueryElement.STAR;
        return this;
    }
    
    public SpathQueryBuilder withType(SpathQueryType type) {
        this.type = type;
        return this;
    }
    
    public SpathQueryBuilder withPredicate(SpathMatch predicate) {
        this.predicate = predicate;
        return this;
    }
    
    public SpathQueryBuilder withPredicate(String name) {
        this.predicate = new SpathPredicateString(name);
        return this;
    }
    
    public SpathQueryBuilder withPredicate(String name, SpathPredicateOperator operator, String value) {
        this.predicate = new SpathPredicateString(name, operator, value);
        return this;
    }
    
    public SpathQueryBuilder withPredicate(String name, SpathPredicateOperator operator, BigDecimal value) {
        this.predicate = new SpathPredicateNumber(name, operator, value);
        return this;
    }
    
    public SpathQueryBuilder withPredicate(String name, SpathPredicateOperator operator, Boolean value) {
        this.predicate = new SpathPredicateBoolean(name, operator, value);
        return this;
    }
    
    public SpathQueryBuilder next() {
        setParent(build());
        return this;
    }
    
    void reset() {
        parent = null;
        name = null;
        type = SpathQueryType.ELEMENT;
        predicate = null;
    }
    
    public SpathQuery build() {
        SpathQueryElement element = null;
        if (parent == null) { 
            if (SpathQueryType.RELATIVE == type) {
                element = new SpathQueryRelative(name, predicate);
            } else if (SpathQueryType.ROOT == type) {
                element = new SpathQueryStart(name, predicate);
            } else {
                throw new IllegalArgumentException("Type at start of path must be ROOT or RELATIVE.");
            }
        } else if (SpathQueryType.RELATIVE == type) {
            element = new SpathQueryRelative(parent, name, predicate);
        } else {
            element = new SpathQueryElement(parent, name, predicate);
        }
        reset();
        type = SpathQueryType.RELATIVE;
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
