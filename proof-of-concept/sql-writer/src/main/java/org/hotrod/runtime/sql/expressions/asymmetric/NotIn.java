package org.hotrod.runtime.sql.expressions.asymmetric;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class NotIn extends AsymmetricalOperator {

  public NotIn(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "not in", subquery);
  }

}
