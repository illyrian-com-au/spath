package org.spath.query;

import org.spath.SpathMatch;
import org.spath.SpathQuery;


public class SpathQueryStart extends SpathQueryElement implements SpathQuery {

    public SpathQueryStart() {
        super(STAR, SpathQueryType.ROOT, null);
    }

    public SpathQueryStart(String name) {
        super(name, SpathQueryType.ROOT, null);
    }

    public SpathQueryStart(SpathMatch predicate) {
        super(STAR, SpathQueryType.ROOT, predicate);
    }

    public SpathQueryStart(String name, SpathMatch predicate) {
        super(name, SpathQueryType.ROOT, predicate);
    }
}
