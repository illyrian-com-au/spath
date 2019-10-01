package org.spath.engine;

import java.math.BigDecimal;

import org.spath.SpathEventSource;
import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.SpathStreamEngine;
import org.spath.query.SpathQueryException;

/**
 * An engine for applying Spath Expressions to a hierarchical series of events of type T.
 *
 * @param <T> an event type read from an input file.
 */
public class SpathStreamEngineImpl<T> extends SpathEngineImpl<T> implements SpathStreamEngine {
    private static final long NO_PROGRESS_THREASHOLD = 1000000;
    
    final SpathEventSource<T> source;
    
    SpathMatch lastMatched = null;
    boolean    firstPass = true;
    long       noProgressThreshold = NO_PROGRESS_THREASHOLD;
    long       noProgressCount = 0;
    
    public SpathStreamEngineImpl(SpathStack<T> stack, SpathEventSource source) {
        super(stack);
        this.source = source;
    }
    
    /**
     * The threshold to detect an infinite loop.
     * Sets the maximum number of times match() can be called without progressing through
     * the input file by calling matchNext(..)
     * @param count the new no progress threshold
     * @return the SpathStreamEngine with the no progress threshold set
     */
    public SpathStreamEngineImpl<T> withNoProgressThreshold(long count) {
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
    
    @Override
    public boolean matchNext(String expr) { 
        SpathQuery query = add(expr);
        return matchNext(query);
    }
    
    @Override
    public boolean matchNext(SpathQuery query) { 
        lastMatched = null;
        noProgressCount = 0;
        while (source.nextEvent()) {
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
                return source.nextEvent();
            }
        } else if (pathMap.isEmpty()) {
            throw new SpathQueryException("No queries have been added to the SpathStreamEngine");
        }
        lastMatched = null;
        noProgressCount = 0;
        while (source.nextEvent()) {
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
        return super.match(query);
    }
    
    @Override
    public String getText() {
        return source.getText();
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
