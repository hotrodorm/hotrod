package org.hotrod.runtime.livesql.dialects;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class DistinctOnRenderer {

  public abstract void render(QueryWriter w, List<Expression> distinctOn);

}
