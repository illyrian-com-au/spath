package org.spath;


public class SpathEventSourceString implements SpathEventSource<String> {
    String [] eventList;
    int index = 0;
    
    public SpathEventSourceString(String [] list) {
        eventList= list;
        index = 0;
    }
    
    public boolean nextEvent(SpathStack<String> stack) {
        if (index < eventList.length) {
            String value = eventList[index++];
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
    
    public String getText(SpathStack<String> engine) {
        if (index < eventList.length) {
            return eventList[index++];
        } else {
            return "";
        }
    }
    
    public boolean hasNext() {
        return index < eventList.length;
    }
}
