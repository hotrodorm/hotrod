package org.hotrod.livesql.dialects;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CastRenderer {

  public abstract void render(QueryWriter w, Expression expr, String type);

}
