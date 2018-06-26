package org.spath.engine;

import java.util.HashMap;
import java.util.Map;

import org.spath.SpathEngine;
import org.spath.SpathEventSource;
import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.query.SpathQueryException;

public class SpathEngineImpl<T> implements SpathEngine {
    final SpathStack<T> stack;
    final SpathEventSource<T> source;
    final Map<String, SpathQuery> pathMap;
    
    SpathMatch lastMatched = null;
    
    public SpathEngineImpl(SpathStack<T> stack, SpathEventSource<T> source) {
        this.stack = stack;
        this.source = source;
        pathMap = new HashMap<String, SpathQuery>();
    }
    
    @Override
    public SpathQuery add(SpathQuery name) {
        pathMap.put(name.toString(), name);
        return name;
    }
    
    @Override
    public boolean matchNext(SpathQuery base) { 
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
    
    public boolean matchNext(String spath) { 
        SpathQuery target = pathMap.get(spath);
        if (target != null) {
            return matchNext(target);
        }
        return false;
    }
    
    @Override
    public boolean matchNext() {
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    public SpathMatch findMatch() {
        for (SpathQuery target : pathMap.values()) {
            if (stack.match(target)) {
                return target;
            }
        }
        return null;
    }
    
    public boolean partial(SpathQuery target) {
        return stack.partial(target);
    }
    
    @Override
    public boolean match(SpathQuery target) {
        if (stack.match(target)) {
            lastMatched = target;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean match(String spath) {
        SpathQuery target = pathMap.get(spath);
        if (target != null) {
            return match(target);
        }
        return false;
    }
    
    @Override
    public String getText() throws SpathQueryException {
        return source.getText(stack);
    }
    
    public String toString() {
        return "Stack: " + stack.toString() + "\nMatched: " + lastMatched;
    }
}
