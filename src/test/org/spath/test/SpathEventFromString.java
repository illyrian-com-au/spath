package org.spath.test;

import org.spath.event.SpathEvent;
import org.spath.event.SpathProperty;

public class SpathEventFromString {
    private static final String START = "(";
    private static final String END = ")";
    private static final String EQUALS = "=";    

    public static SpathEvent [] convert(String [] list) {
        SpathEvent [] results = new SpathEvent[list.length];
        for (int i=0; i<list.length; i++) {
            if (list[i] != null) {
            results[i] = toEvent(list[i]);
            } else {
                results[i] = null;
            }
        }
        return results;
    }
    
    public static SpathEvent toEvent(String value) {
        String name = getName(value);
        SpathEvent event = new SpathEvent(name);
        String attrString = getAttributesAsString(value);
        String [] attributes = toArray(attrString, ",", 0);
        for (String attr : attributes) {
            SpathProperty property = toProperty(attr);
            event.addProperty(property);
        }
        return event;
    }
    
    public static SpathProperty toProperty(String propertyString) {
        String name = getAttributeName(propertyString);
        String value = getAttributeValue(propertyString);
        return new SpathProperty(name, value);
    }
    
    private static String getName(String event) {
        int endIndex = event.indexOf(START);
        return endIndex >= 0 ? event.substring(0, endIndex).trim() : event;
    }
    
    private static String getAttributesAsString(String event) {
        int startIndex = event.indexOf(START);
        int endIndex = event.indexOf(END);
        if (startIndex >= 0 && endIndex >= 0) {
            return event.substring(startIndex + START.length(), endIndex).trim();
        } else {
            return "";
        }
    }

    private static String [] toArray(String event, String start, int startIndex) {
        if (event == null || event.length() == 0) {
            return new String[0];
        }
        String [] attributes = null;
        int endIndex = event.indexOf(start, 1);
        if (endIndex >= 0) {
            String attr = event.substring(0, endIndex);
            String remainder = event.substring(endIndex + start.length());
            attributes = toArray(remainder, start, startIndex+1);
            attributes[startIndex] = attr;
        } else {
            attributes = new String[startIndex+1];
            attributes[startIndex] = event;
        }
        return attributes;
    }
    
    private static String getAttributeName(String attribute) {
        int index = attribute.indexOf(EQUALS);
        if (index >= 0) {
            return attribute.substring(0, index).trim();
        } else {
            throw new IllegalArgumentException("Attribute must contain '=' : " + attribute);
        }
    }
    
    private static String getAttributeValue(String attribute) {
        int index = attribute.indexOf(EQUALS);
        if (index >= 0) {
            String value = attribute.substring(index+EQUALS.length(), attribute.length()).trim();
            // Remove quotes
            return value.substring(1, value.length()-1);
        } else {
            throw new IllegalArgumentException("Attribute must contain '=' : " + attribute);
        }
    }

}
