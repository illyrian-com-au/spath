package org.spath.engine;

import java.math.BigDecimal;

public class SpathUtil {
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public static Boolean getValueAsBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            return getValueAsBoolean((String)value);
        }
        return null;
    }
    
    public static Boolean getValueAsBoolean(String valueString) {
        if (TRUE.equalsIgnoreCase(valueString)) {
            return Boolean.TRUE;
        } else if (FALSE.equalsIgnoreCase(valueString)) {
            return Boolean.FALSE;
        }
        return null;
    }

    public static BigDecimal getValueAsNumber(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        } else if (value instanceof String) {
            return getValueAsNumber((String) value);
        }
        return null;
    }
    
    public static BigDecimal getValueAsNumber(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
    

}
