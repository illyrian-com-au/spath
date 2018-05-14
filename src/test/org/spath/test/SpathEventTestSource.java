package org.spath.test;

import org.spath.SpathEventSource;
import org.spath.SpathStack;
import org.spath.data.SpathEvent;


public class SpathEventTestSource implements SpathEventSource<SpathEvent> {
    SpathEvent [] eventList;
    int index = -1;
    
    public SpathEventTestSource(String [] list) {
        this(SpathEventFromString.convert(list));
    }
    
    public SpathEventTestSource(SpathEvent [] list) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        eventList = list;
        index = -1;
    }
    
    public boolean hasNext() {
        return index < eventList.length;
    }
    
    public boolean nextEvent(SpathStack<SpathEvent> stack) {
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
    
    public String getText(SpathStack<SpathEvent> engine) {
        if (index < eventList.length) {
            return eventList[index].getText();
        } else {
            return "";
        }
    }
}
