package org.spath;

import java.math.BigDecimal;


final class SpathEvaluatorString implements SpathEvaluator<String> {
    private static final String START = "[@";
    private static final String EQUALS = "=";    
    
    @Override
    public boolean match(SpathNameElement target, String event) {
        return target.getName().equals(getName(event));
    }

    @Override
    public boolean match(SpathNameRelative target, String event) {
        return target.getName().equals(getName(event));
    }

    @Override
    public boolean match(SpathNameStar target, String event) {
        return true;
    }

    @Override
    public boolean match(SpathPredicateString predicate, String event) {
        String [] attributes = toArray(getAttributesAsString(event), START, 0);
        for (String attr : attributes) {
            if (matchAttribute(predicate, attr)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean matchAttribute(SpathPredicateString predicate, String attribute) {
        String attributeName = getAttributeName(attribute);
        String attributeValue = getAttributeValue(attribute);
        return predicate.compareTo(attributeName, attributeValue);
    }
    
    @Override
    public boolean match(SpathPredicateNumber predicate, String event) {
        String [] attributes = toArray(getAttributesAsString(event), START, 0);
        for (String attr : attributes) {
            if (matchAttribute(predicate, attr)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchAttribute(SpathPredicateNumber predicate, String attribute) {
        String attributeName = getAttributeName(attribute);
        if (predicate.getName().equals(attributeName)) {
            String attributeValue = getAttributeValue(attribute);
            BigDecimal attributeNumber = toBigDecimal(attributeValue);
            return predicate.compareTo(attributeName, attributeNumber);
        }
        return false;
    }
    
    @Override
    public boolean match(SpathPredicateBoolean target, String event) {
        throw new IllegalArgumentException("SpathPredicateBoolean not handled");
    }

    private BigDecimal toBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
    
    private String getName(String event) {
        int endIndex = event.indexOf(START);
        return endIndex >= 0 ? event.substring(0, endIndex) : event;
    }
    
    private String getAttributesAsString(String event) {
        int endIndex = event.indexOf(START);
        return endIndex >= 0 ? event.substring(endIndex) : "";
    }

    private String [] toArray(String event, String start, int index) {
        if (event == null || event.length() == 0) {
            return new String[0];
        }
        String [] attributes = null;
        int endIndex = event.indexOf(start, 1);
        if (endIndex >= 0) {
            String attr = event.substring(0, endIndex);
            String remainder = event.substring(endIndex);
            attributes = toArray(remainder, start, index+1);
            attributes[index] = attr;
        } else {
            attributes = new String[index+1];
            attributes[index] = event;
        }
        return attributes;
    }
    
    private String getAttributeName(String attribute) {
        int index = attribute.indexOf(EQUALS);
        if (index >= 0) {
            return attribute.substring(2, index);
        } else {
            throw new IllegalArgumentException("Attribute must contain '=' : " + attribute);
        }
    }
    
    private String getAttributeValue(String attribute) {
        int index = attribute.indexOf(EQUALS);
        if (index >= 0) {
            return attribute.substring(index+2, attribute.length()-2);
        } else {
            throw new IllegalArgumentException("Attribute must contain '=' : " + attribute);
        }
    }

}