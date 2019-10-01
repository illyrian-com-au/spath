package org.spath.query;

import org.spath.SpathMatch;
import org.spath.SpathQuery;


public class SpathQueryStart extends SpathQueryElement implements SpathQuery {

    public SpathQueryStart() {
        super(new SpathAnyName(), SpathQueryType.ROOT, null);
    }

    public SpathQueryStart(SpathName name) {
        super(name, SpathQueryType.ROOT, null);
    }

    public SpathQueryStart(SpathName name, SpathMatch predicate) {
        super(name, SpathQueryType.ROOT, predicate);
    }
}
