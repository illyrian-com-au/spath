package org.spath;

import java.math.BigDecimal;

public interface SpathStreamEngine extends SpathEngine {

    /** Advance to the next event that matches any query known to the engine */
    boolean matchNext();

    /** Advance to the next event that includes a path that matches the given query */
    boolean matchNext(SpathQuery query);

    /** Advance to the next event that includes a path that matches the Spath expression */
    boolean matchNext(String expr);

    /** Value as a text string */
    String getText();
    
    /** Value as a decimal number */
    BigDecimal getDecimal();

    /** Value as a boolean */
    Boolean getBoolean();
}