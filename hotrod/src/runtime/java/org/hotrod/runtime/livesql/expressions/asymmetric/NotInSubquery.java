package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.expressions.Expression;

public class NotInSubquery extends AsymmetricOperator {

  public NotInSubquery(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "not in", subquery);
  }

}
