package org.spath;

import java.util.Stack;

public class SpathStack<T> {
    Stack<T> stack = new Stack<>();
    SpathEvaluator<T> matcher;

    public SpathStack(SpathEvaluator<T> matcher) {
        this.matcher = matcher;
    }
    
    public boolean match(SpathName target) {
        if (target == null) {
            return true;
        } else if (matchTarget(target, target.getOffset())){
            return match(target.getParent());
        } else {
            return false;
        }
    }
    
    public boolean matchTarget(SpathName target, int offset) {
        if (target == null) {
            return true;
        } else if (offset >= 0 && offset < stack.size()) {
            T event = stack.get(offset);
            return target.match(matcher, event);
        } else if (offset < 0 && -offset <= stack.size()) {
            T event = stack.get(stack.size() + offset);
            return target.match(matcher, event);
        } else {
            // The stack is not large enough to match this part of the spath.
            return false;
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
    
    public String toString() {
        return stack.toString();
    }
}
