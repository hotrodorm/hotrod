package org.hotrod.runtime.sql.expressions.asymmetric;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class GeAll extends AsymmetricalOperator {

  public GeAll(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, ">= all", subquery);
  }

}
