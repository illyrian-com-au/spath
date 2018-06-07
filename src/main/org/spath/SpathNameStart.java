package org.spath;


public class SpathNameStart extends SpathNameElement implements SpathName {

    public SpathNameStart() {
        super(STAR, SpathType.ROOT);
    }

    public SpathNameStart(String name) {
        super(name, SpathType.ROOT);
    }
}
