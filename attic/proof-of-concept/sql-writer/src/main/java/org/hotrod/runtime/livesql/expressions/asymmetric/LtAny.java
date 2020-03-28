package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.expressions.Expression;

public class LtAny extends AsymmetricOperator {

  public LtAny(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "< any", subquery);
  }

}
