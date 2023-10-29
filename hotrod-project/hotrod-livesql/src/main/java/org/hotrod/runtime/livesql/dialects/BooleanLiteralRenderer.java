package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class BooleanLiteralRenderer {

  public abstract void renderTrue(LiveSQLContext context, QueryWriter w);

  public abstract void renderFalse(LiveSQLContext context, QueryWriter w);

}
