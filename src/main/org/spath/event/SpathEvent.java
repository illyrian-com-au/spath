package org.spath.event;

import java.util.ArrayList;
import java.util.List;

public class SpathEvent {
    private final String name;
    private final List<SpathProperty> properties;
    private String text;
    
    public SpathEvent(String name, List<SpathProperty> properties) {
        this.name = name;
        this.properties = properties;
    }

    public SpathEvent(String name) {
        this.name = name;
        properties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    
    public List<SpathProperty> getProperties() {
        return properties;
    }
    
    public String getText() {
        return text;
    }

    public SpathEvent setText(String text) {
        this.text = text;
        return this;
    }
    
    private String toString(List<SpathProperty> list) {
        StringBuilder build = new StringBuilder();
        build.append('(');
        for (SpathProperty property : list) {
            if (build.length() > 1) {
                build.append(", ");
            }
            build.append(property);
        }
        build.append(')');
        return build.toString();
    }
    
    public String toString() {
        if (properties == null || properties.isEmpty()) {
            return getName();
        } else {
            return getName() + toString(getProperties());
        }
    }
}
