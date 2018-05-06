package org.spath.impl;

import java.util.ArrayList;
import java.util.List;

public class SpathElement {
    private final String name;
    private final ArrayList<SpathProperty> properties = new ArrayList<>();
    private String text;
    
    public SpathElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public List<SpathProperty> getProperties() {
        return properties;
    }
    
    public SpathElement addProperty(SpathProperty property) {
        properties.add(property);
        return this;
    }
    
    public SpathElement addProperty(String name, Object value) {
        return addProperty(new SpathProperty(name, value));
    }


    public String getText() {
        return text;
    }

    public SpathElement setText(String text) {
        this.text = text;
        return this;
    }
}
