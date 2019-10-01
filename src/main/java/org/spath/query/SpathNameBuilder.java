package org.spath.query;

public class SpathNameBuilder {
    private String qualifier;
    private String name;
    private String function;
    private boolean isStar = false;

    public SpathNameBuilder() {
    }
    
    public String getQualifier() {
        return qualifier;
    }

    public SpathNameBuilder withQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public String getName() {
        return name;
    }

    public SpathNameBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getFunction() {
        return function;
    }

    public SpathNameBuilder withFunction(String function) {
        this.function = function;
        return this;
    }

    SpathName build() {
        if (isStar) {
            return new SpathAnyName();
        } else if (qualifier != null) {
            return new SpathQualifiedName(qualifier, name);
        } else if (function != null) {
            return new SpathFunction(function);
        } else {
            return new SpathName(name);
        }
    }
}
