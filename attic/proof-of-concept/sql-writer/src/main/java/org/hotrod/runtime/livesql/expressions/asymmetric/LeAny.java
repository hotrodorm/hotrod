package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.expressions.Expression;

public class LeAny extends AsymmetricOperator {

  public LeAny(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "<= any", subquery);
  }

}
