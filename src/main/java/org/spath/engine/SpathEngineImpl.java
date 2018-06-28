package org.spath.engine;

import java.util.HashMap;
import java.util.Map;

import org.spath.SpathEngine;
import org.spath.SpathEventSource;
import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.parser.SpathParser;
import org.spath.query.SpathQueryException;

public class SpathEngineImpl<T> implements SpathEngine {
    final SpathStack<T> stack;
    final SpathEventSource<T> source;
    final Map<String, SpathQuery> pathMap;
    final SpathParser parser = createSpathParser();
    
    SpathMatch lastMatched = null;
    boolean firstPass = true;
    
    public SpathEngineImpl(SpathStack<T> stack, SpathEventSource<T> source) {
        this.stack = stack;
        this.source = source;
        pathMap = new HashMap<String, SpathQuery>();
    }
    
    protected SpathParser createSpathParser() {
        return new SpathParser();
    }
    
    
    protected SpathMatch findMatch() {
        for (SpathQuery target : pathMap.values()) {
            if (stack.match(target)) {
                return target;
            }
        }
        return null;
    }
    
    SpathQuery get(String expr) {
        return pathMap.get(expr);
    }
    
    @Override
    public SpathQuery query(String expr) {
        SpathQuery query = pathMap.get(expr);
        if (query == null) {
            query = parser.parse(expr);
            query(query);
        }
        return query;
    }
    
    @Override
    public SpathQuery query(SpathQuery query) {
        pathMap.put(query.toString(), query);
        return query;
    }
    
    @Override
    public boolean matchNext(String expr) { 
        SpathQuery query = query(expr);
        return matchNext(query);
    }
    
    @Override
    public boolean matchNext(SpathQuery query) { 
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if (query != null && !stack.partial(query)) {
                break;
            } else if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean matchNext() {
        if (firstPass) {
            firstPass = false;
            if (pathMap.isEmpty()) {
                source.nextEvent(stack);
                return true;
            }
        }
        lastMatched = null;
        while (source.nextEvent(stack)) {
            if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean match(SpathQuery query) {
        if (stack.match(query)) {
            lastMatched = query;
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean match(String expr) {
        SpathQuery query = query(expr);
        if (query != null) {
            return match(query);
        }
        return false;
    }
    
    @Override
    public String getText() throws SpathQueryException {
        return source.getText(stack);
    }
    
    @Override
    public String toString() {
        return "Stack: " + stack.toString() + "\nMatched: " + lastMatched;
    }
}
