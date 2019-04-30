package org.hotrod.runtime.sql.expressions.asymmetric;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class EqAll extends AsymmetricalOperator {

  public EqAll(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "= all", subquery);
  }

}
