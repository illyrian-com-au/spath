package org.spath.data;

import java.util.ArrayList;
import java.util.List;

public class SpathEvent {
    private final String name;
    private final ArrayList<SpathProperty> properties = new ArrayList<>();
    private String text;
    
    public SpathEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public List<SpathProperty> getProperties() {
        return properties;
    }
    
    public SpathEvent addProperty(SpathProperty property) {
        properties.add(property);
        return this;
    }
    
    public SpathEvent addProperty(String name, Object value) {
        return addProperty(new SpathProperty(name, value));
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
