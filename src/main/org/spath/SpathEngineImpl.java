package org.spath;

import java.util.HashMap;
import java.util.Map;

public class SpathEngineImpl<T> implements SpathEngine {
    final SpathStack<T> stack;
    final SpathEventSource<T> source;
    final Map<String, SpathName> pathMap;
    
    SpathName lastMatched = null;
    
    public SpathEngineImpl(SpathStack<T> stack, SpathEventSource<T> source) {
        this.stack = stack;
        this.source = source;
        pathMap = new HashMap<>();
    }
    
    @Override
    public SpathName add(SpathName name) {
        pathMap.put(name.toString(), name);
        return name;
    }
    
    @Override
    public boolean hasNext() {
        return source.hasNext();
    }
    
    @Override
    public boolean matchAny(SpathName base) throws SpathException {
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if (!match(base)) {
                break;
            } else if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean matchAny() throws SpathException {
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if ((lastMatched  = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public SpathName findMatch() {
        for (SpathName target : pathMap.values()) {
            if (stack.match(target)) {
                return target;
            }
        }
        return null;
    }
    
    @Override
    public boolean match(SpathName target) {
        if (stack.match(target)) {
            lastMatched = target;
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public SpathName getLastMatched() {
        return lastMatched;
    }
    
    @Override
    public String getText() throws SpathException {
        return source.getText(stack);
    }
    
    public String toString() {
        return "Stack: " + stack.toString() + "\nMatched: " + lastMatched;
    }
}
