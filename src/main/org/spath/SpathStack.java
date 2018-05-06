package org.spath;

import java.util.Stack;

public class SpathStack<T> {
    Stack<T> stack = new Stack<>();
    SpathEvaluator<T> matcher;

    public SpathStack(SpathEvaluator<T> matcher) {
        this.matcher = matcher;
    }
    
    public boolean match(SpathName target) {
        return matchTarget(target, stack.size() - 1);
    }
    
    public boolean partial(SpathName target) {
        for (int i=stack.size(); target.getDepth()<=i; i--) {
            if (matchTarget(target, i - 1)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean matchTarget(SpathName target, int index) {
        if (target == null) {
            return true; // All targets matched
        } else if (size() < target.getDepth()) {
            return false; // Stack is not deep enough
        } else {
            T event = getEvent(target, index);
            if (event != null && target.match(matcher, event)) {
                return matchTarget(target.getParent(), index - 1);
            }
            return false;
        }
    }

    private T getEvent(SpathName target, int index) {
        if (target.getType() == SpathType.ROOT) {
            return get(target.getDepth() - 1);
        } else {
            return get(index);
        }
    }

    public void push(T event) {
        stack.push(event);
    }
    
    public T pop() {
        return stack.pop();
    }
    
    public T peek() {
        return stack.isEmpty() ? null : stack.peek();
    }
    
    public T get(int index) {
        int size = stack.size();
        if (index >= 0 && index < size) {
            T event = stack.get(index);
            return event;
        } else {
            return null;
        }
    }
    
    public int size() {
        return stack.size();
    }
    
    public String toString() {
        return stack.toString();
    }
}
