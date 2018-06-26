package org.spath;

import org.spath.query.SpathQueryType;

public interface SpathQuery extends SpathMatch {

    SpathQuery getParent();

    int getDepth();
    
    SpathQueryType getType();
    
    SpathMatch getPredicate();
}