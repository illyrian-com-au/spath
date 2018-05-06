package org.spath.impl;

import java.math.BigDecimal;

public class SpathProperty {
    private static final String TRUE = "true";
    private static final String FALSE = "false";
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
    
    public BigDecimal getValueAsNumber() {
        if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        } else if (value instanceof String) {
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException nfe) {
                // Fall through
            }
        }
        return null;
    }
    
    public Boolean getValueAsBoolean() {
        if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            String valueString = value.toString();
            if (TRUE.equalsIgnoreCase(valueString)) {
                return Boolean.TRUE;
            } else if (FALSE.equalsIgnoreCase(valueString)) {
                return Boolean.FALSE;
            }
        }
        return null;
    }
    
    private String encode(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + value.toString() + "\"";
        } else if (value instanceof Character) {
            return "\'" + value.toString() + "\'";
        } else {
            return value.toString();
        }
    }

    @Override
    public String toString() {
        return name + "=" + encode(value);
    }
}
