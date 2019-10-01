package org.spath.test;

import org.spath.SpathEventSource;
import org.spath.SpathStack;
import org.spath.event.SpathEvent;

public class SpathEventTestSource implements SpathEventSource<SpathEvent> {
    SpathEvent [] eventList;
    SpathStack<SpathEvent> stack;
    int index = -1;
    
    public SpathEventTestSource(SpathEvent [] list, SpathStack<SpathEvent> stack) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        eventList = list;
        this.stack = stack;
        index = -1;
    }
    
    public boolean hasNext() {
        return index < eventList.length;
    }
    
    public boolean nextEvent() {
        if (index + 1 < eventList.length) {
            index++;
            SpathEvent value = eventList[index];
            if (value == null) {
                stack.pop();
            } else {
                stack.push(value);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public String getText() {
        if (index < eventList.length) {
            return eventList[index].getText();
        } else {
            return "";
        }
    }
}
