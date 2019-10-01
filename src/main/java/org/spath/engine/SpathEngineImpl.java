package org.spath.engine;

import java.util.HashMap;
import java.util.Map;

import org.spath.SpathEngine;
import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.parser.SpathParser;

/**
 * An engine for applying Spath Expressions to a hierarchical series of events of type T.
 *
 * @param <T> an event type read from an input file.
 */
public class SpathEngineImpl<T> implements SpathEngine {
    
    final SpathStack<T> stack;
    final Map<String, SpathQuery> pathMap;
    final SpathParser parser = createSpathParser();
    
    SpathMatch lastMatched = null;
    
    public SpathEngineImpl(SpathStack<T> stack) {
        this.stack = stack;
        pathMap = new HashMap<String, SpathQuery>();
    }
    
    public SpathStack<T> getStack() {
        return stack;
    }
    
    protected SpathParser createSpathParser() {
        return new SpathParser();
    }
    
    @Override
    public SpathMatch findMatch() {
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
    public SpathQuery add(String expr) {
        SpathQuery query = pathMap.get(expr);
        if (query == null) {
            query = parser.parse(expr);
            add(query);
        }
        return query;
    }
    
    @Override
    public SpathQuery add(SpathQuery query) {
        pathMap.put(query.toString(), query);
        return query;
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
        SpathQuery query = add(expr);
        if (query != null) {
            return match(query);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Stack: " + stack.toString() + "\nMatched: " + lastMatched;
    }
}
