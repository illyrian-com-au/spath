package org.spath;

import java.util.HashMap;
import java.util.Map;

public class SpathEngineImpl<T> implements SpathEngine {
    final SpathStack<T> stack;
    final SpathEventSource<T> source;
    final Map<String, SpathName> pathMap;
    
    SpathMatch lastMatched = null;
    
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
    public boolean matchNext(SpathName base) throws SpathException {
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if (base != null && !stack.partial(base)) {
                break;
            } else if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    public boolean matchNext(String spath) throws SpathException {
        SpathName target = pathMap.get(spath);
        if (target != null) {
            return matchNext(target);
        }
        return false;
    }
    
    @Override
    public boolean matchNext() throws SpathException {
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    public SpathMatch findMatch() {
        for (SpathName target : pathMap.values()) {
            if (stack.match(target)) {
                return target;
            }
        }
        return null;
    }
    
    public boolean partial(SpathName target) {
        return stack.partial(target);
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
    
    public boolean match(String spath) {
        SpathName target = pathMap.get(spath);
        if (target != null) {
            return match(target);
        }
        return false;
    }
    
    @Override
    public String getText() throws SpathException {
        return source.getText(stack);
    }
    
    public String toString() {
        return "Stack: " + stack.toString() + "\nMatched: " + lastMatched;
    }
}
