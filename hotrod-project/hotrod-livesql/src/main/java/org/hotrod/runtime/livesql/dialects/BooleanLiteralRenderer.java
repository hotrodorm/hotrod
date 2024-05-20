package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class BooleanLiteralRenderer {

  public abstract void renderTrue(QueryWriter w);

  public abstract void renderFalse(QueryWriter w);

}
