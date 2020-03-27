package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class LtAll extends AsymmetricOperator {

  public LtAll(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "< all", subquery);
  }

}
