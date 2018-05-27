package org.spath.data;

import java.math.BigDecimal;

public class SpathProperty {
    private final String name;
    private final Object value;
    
    public SpathProperty(String name, Object value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
    
    public String getValueAsString() {
        if (value instanceof String) {
            return (String)value;
        }
        return null;
    }
    
    private String encode(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "'" + value.toString() + "'";
        } else {
            return value.toString();
        }
    }

    @Override
    public String toString() {
        return name + "=" + encode(value);
    }
}
