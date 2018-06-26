package org.spath;

import org.spath.query.SpathQueryException;

public interface SpathEngine {

    boolean matchNext(SpathQuery base) throws SpathQueryException;

    boolean matchNext() throws SpathQueryException;

    boolean match(SpathQuery target);

    SpathQuery add(SpathQuery name);

    String getText() throws SpathQueryException;

}