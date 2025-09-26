package org.hotrod.livesql.dialects;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class BooleanLiteralRenderer {

  public abstract void renderTrue(QueryWriter w);

  public abstract void renderFalse(QueryWriter w);

}
