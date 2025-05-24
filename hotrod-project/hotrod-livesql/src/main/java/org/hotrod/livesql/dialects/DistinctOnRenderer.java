package org.hotrod.livesql.dialects;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class DistinctOnRenderer {

  public abstract void render(QueryWriter w, List<Expression> distinctOn);

}
