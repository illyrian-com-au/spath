package org.spath.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpathEventBuilder {
    private String name;
    private String text;
    private List<SpathProperty> properties;
    
    public SpathEventBuilder(){
        reset();
    }
    
    public SpathEventBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public SpathEventBuilder withProperty(SpathProperty property) {
        properties.add(property);
        return this;
    }
    
    public SpathEventBuilder withProperty(String name, String value) {
        SpathProperty property = new SpathProperty(name, value);
        properties.add(property);
        return this;
    }
    
    public SpathEventBuilder withProperty(String name, BigDecimal value) {
        SpathProperty property = new SpathProperty(name, value);
        properties.add(property);
        return this;
    }
    
    public SpathEventBuilder withProperty(String name, Boolean value) {
        SpathProperty property = new SpathProperty(name, value);
        properties.add(property);
        return this;
    }
    
    public SpathEventBuilder withText(String text) {
        this.text = text;
        return this;
    }
    
    public void reset() {
        name = null;
        text = null;
        properties = new ArrayList<>();
    }
    
    public SpathEvent build() {
        SpathEvent event = new SpathEvent(name, properties);
        event.setText(text);
        reset();
        return event;
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("name=").append(name).append("(");
        for (SpathProperty property : properties) {
            build.append(property).append(" ");
        }
        build.append(")");
        if (text != null) {
            build.append(text);
        }
        return build.toString();
    }
}
