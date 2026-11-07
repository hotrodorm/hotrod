package org.hotrod.livesql.functions;

import org.hotrod.livesql.queries.QueryWriter;

public abstract class PreparedSegment {

  abstract void renderTo(final QueryWriter w);

}
