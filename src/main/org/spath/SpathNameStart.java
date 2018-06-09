package org.spath;


public class SpathNameStart extends SpathNameElement implements SpathName {

    public SpathNameStart() {
        super(STAR, SpathType.ROOT, null);
    }

    public SpathNameStart(String name) {
        super(name, SpathType.ROOT, null);
    }

    public SpathNameStart(SpathMatch predicate) {
        super(STAR, SpathType.ROOT, predicate);
    }

    public SpathNameStart(String name, SpathMatch predicate) {
        super(name, SpathType.ROOT, predicate);
    }
}
