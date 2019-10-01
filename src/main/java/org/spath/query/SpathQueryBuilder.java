package org.spath.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.spath.SpathMatch;
import org.spath.SpathQuery;

public class SpathQueryBuilder {
    private SpathQuery parent = null;
    private SpathName name = null;
    private SpathQueryType type = SpathQueryType.RELATIVE;
    private SpathMatch predicate;
    
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
    
    public SpathQueryBuilder root() {
        withType(SpathQueryType.ROOT);
        return this;
    }
    
    public SpathQueryBuilder relative() {
        withType(SpathQueryType.RELATIVE);
        return this;
    }
    
    public SpathQueryBuilder withElement(SpathName name) {
        this.name = name;
        return this;
    }
    
    public SpathQueryBuilder withName(String name) {
        if (SpathAnyName.STAR.equals(name)) {
            return withStar();
        }
        return withElement(new SpathName(name));
    }
    
    public SpathQueryBuilder withName(String qualifier, String name) {
        return withElement(new SpathQualifiedName(qualifier, name));
    }
    
    public SpathQueryBuilder withStar() {
        return withElement(new SpathAnyName());
    }
    
    public SpathQueryBuilder withFunction(String name) {
        return withElement(new SpathFunction(name));
    }
    
    public SpathQueryBuilder withType(SpathQueryType type) {
        this.type = type;
        return this;
    }
    
    public SpathQueryBuilder andPredicate(SpathMatch otherPredicate) {
        if (this.predicate != null) {
            this.predicate = new SpathPredicateAnd(this.predicate, otherPredicate);
        } else {
            this.predicate = otherPredicate;
        }
        return this;
    }
    
    public SpathQueryBuilder orPredicate(SpathMatch otherPredicate) {
        this.predicate = new SpathPredicateOr(this.predicate, otherPredicate);
        return this;
    }
    
    public SpathQueryBuilder withPredicate(SpathMatch predicate) {
        return andPredicate(predicate);
    }
    
    public SpathQueryBuilder withPredicate(String name) {
        return andPredicate(new SpathPredicateString(name));
    }
    
    public SpathQueryBuilder withPredicate(String name, SpathPredicateOperator operator, String value) {
        return andPredicate(new SpathPredicateString(name, operator, value));
    }
    
    public SpathQueryBuilder withPredicate(String name, SpathPredicateOperator operator, BigDecimal value) {
        return andPredicate(new SpathPredicateNumber(name, operator, value));
    }
    
    public SpathQueryBuilder withPredicate(String name, SpathPredicateOperator operator, Boolean value) {
        return andPredicate(new SpathPredicateBoolean(name, operator, value));
    }
    
    public SpathQueryBuilder next() {
        setParent(build());
        return this;
    }
    
    void reset() {
        parent = null;
        name = null;
        type = SpathQueryType.RELATIVE;
        predicate = null;
    }
    
    public SpathQuery build() {
        // FIXME name.isTerminal()
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
        } else if (name.isTerminal()) {
            element = new SpathQueryTerminal(parent, name);
        } else {
            element = new SpathQueryElement(parent, name, predicate);
        }
        reset();
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
