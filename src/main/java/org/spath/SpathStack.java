package org.spath;


public interface SpathStack<T> {

    boolean match(SpathQuery target);

    boolean partial(SpathQuery target);

    void push(T event);

    T pop();

    T peek();

    T get(int index);

    int size();

}