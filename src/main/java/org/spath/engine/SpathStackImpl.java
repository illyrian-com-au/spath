package org.spath.engine;

import java.util.Stack;

import org.spath.SpathEvaluator;
import org.spath.SpathQuery;
import org.spath.SpathStack;
import org.spath.query.SpathQueryType;

public class SpathStackImpl<T> implements SpathStack<T> {
    Stack<T> stack = new Stack<T>();
    SpathEvaluator<T> evaluator;

    public SpathStackImpl(SpathEvaluator<T> matcher) {
        this.evaluator = matcher;
    }
    
    public SpathStackImpl() {
    }
    
    public void setSpathEvaluator(SpathEvaluator<T> matcher) {
        this.evaluator = matcher;
    }
    
    public SpathEvaluator<T> setSpathEvaluator() {
        return evaluator;
    }
    
    @Override
    public boolean match(SpathQuery target) {
        return matchTarget(target, stack.size() - 1);
    }
    
    @Override
    public boolean partial(SpathQuery target) {
        // Match the target anywhere in the stack
        for (int i=stack.size(); target.getDepth()<=i; i--) {
            if (matchTarget(target, i - 1)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean matchTarget(SpathQuery target, int index) {
        if (target == null) {
            // Exit condition for recursive loop - whole SpathQuery has been matched
            return true;
        } else if (size() < target.getDepth()) {
            // Shortcut failure - stack is not deep enough to match target
            return false; 
        } else {
            // Match the event in the stack at the current level
            T event = getEvent(target, index);
            if (event != null && target.match(evaluator, event)) {
                // Recursive call to match the parent level of the Spath query
                return matchTarget(target.getParent(), index - 1);
            }
            return false;
        }
    }

    private T getEvent(SpathQuery target, int index) {
        if (target.getType() == SpathQueryType.ROOT) {
            return get(target.getDepth() - 1);
        } else {
            return get(index);
        }
    }

    @Override
    public void push(T event) {
        stack.push(event);
    }
    
    @Override
    public T pop() {
        return stack.pop();
    }
    
    @Override
    public T peek() {
        return stack.isEmpty() ? null : stack.peek();
    }
    
    @Override
    public T get(int index) {
        int size = stack.size();
        if (index >= 0 && index < size) {
            T event = stack.get(index);
            return event;
        } else {
            return null;
        }
    }
    
    @Override
    public int size() {
        return stack.size();
    }
    
    public String toString() {
        return stack.toString();
    }
}
