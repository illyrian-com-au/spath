package org.spath.engine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.spath.SpathEngine;
import org.spath.SpathEventSource;
import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.parser.SpathParser;
import org.spath.query.SpathQueryException;

/**
 * An engine for applying Spath Expressions to a hierarchical series of events of type T.
 *
 * @param <T> an event type read from an input file.
 */
public class SpathEngineImpl<T> implements SpathEngine {
    private static final long NO_PROGRESS_THREASHOLD = 1000000;
    
    final SpathStack<T> stack;
    final SpathEventSource<T> source;
    final Map<String, SpathQuery> pathMap;
    final SpathParser parser = createSpathParser();
    
    SpathMatch lastMatched = null;
    boolean    firstPass = true;
    long       noProgressThreshold = NO_PROGRESS_THREASHOLD;
    long       noProgressCount = 0;
    
    public SpathEngineImpl(SpathStack<T> stack, SpathEventSource<T> source) {
        this.stack = stack;
        this.source = source;
        pathMap = new HashMap<String, SpathQuery>();
    }
    
    /**
     * The threshold to detect an infinite loop.
     * Sets the maximum number of times match() can be called without progressing through
     * the input file by calling matchNext(..)
     * @param count the new no progress threshold
     * @return the SpathEngine with the no progress threshold set
     */
    public SpathEngineImpl<T> withNoProgressThreshold(long count) {
        noProgressThreshold = count;
        return this;
    }
    
    /**
     * The threshold to detect an infinite loop.
     * The maximum number of times match() can be called without progressing through
     * the input file by calling matchNext(..)
     * Defaults to 1,000,000.
     */
    public long getNoProgressTreshold() {
        return noProgressThreshold;
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
        noProgressCount = 0;
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
        // Allow one pass for queries to be added to the engine
        if (firstPass) {
            firstPass = false;
            if (pathMap.isEmpty()) {
                return source.nextEvent(stack);
            }
        } else if (pathMap.isEmpty()) {
            throw new SpathQueryException("No queries have been added to the SpathEngine");
        }
        lastMatched = null;
        noProgressCount = 0;
        while (source.nextEvent(stack)) {
            if ((lastMatched = findMatch()) != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean match(SpathQuery query) {
        if (++noProgressCount > noProgressThreshold) {
            throw new SpathQueryException("Infinite loop detected. Do not use while (match(query)) ...");
        }
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
    public String getText() {
        return source.getText(stack);
    }
    
    @Override
    public BigDecimal getDecimal() {
        return SpathUtil.getValueAsNumber(getText());
    }

    @Override
    public Boolean getBoolean() {
        return SpathUtil.getValueAsBoolean(getText());
    }
    
    @Override
    public String toString() {
        return "Stack: " + stack.toString() + "\nMatched: " + lastMatched;
    }
}
