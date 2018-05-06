package org.spath.impl;

import org.spath.SpathEventSource;
import org.spath.SpathStack;


public class SpathElementEventSource implements SpathEventSource<SpathElement> {
    SpathElement [] eventList;
    int index = -1;
    
    public SpathElementEventSource(SpathElement [] list) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        eventList = list;
        index = -1;
    }
    
    public boolean hasNext() {
        return index < eventList.length;
    }
    
    public boolean nextEvent(SpathStack<SpathElement> stack) {
        if (index + 1 < eventList.length) {
            index++;
            SpathElement value = eventList[index];
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
    
    public String getText(SpathStack<SpathElement> engine) {
        if (index < eventList.length) {
            return eventList[index].getText();
        } else {
            return "";
        }
    }
}
