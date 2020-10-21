package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class GeAll extends AsymmetricOperator {

  public GeAll(final Expression<?> value, final ExecutableSelect<?> subquery) {
    super(value, ">= all", subquery);
  }

}
