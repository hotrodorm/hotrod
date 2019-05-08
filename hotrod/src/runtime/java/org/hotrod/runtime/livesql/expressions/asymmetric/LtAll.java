package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.expressions.Expression;

public class LtAll extends AsymmetricOperator {

  public LtAll(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "< all", subquery);
  }

}
