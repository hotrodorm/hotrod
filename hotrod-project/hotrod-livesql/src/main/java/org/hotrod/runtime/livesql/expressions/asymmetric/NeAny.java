package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class NeAny extends AsymmetricOperator {

  public NeAny(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "<> any", subquery);
  }

}
