package org.hotrod.runtime.sql.expressions.asymmetric;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class In extends AsymmetricalOperator {

  public In(final Expression value, final ExecutableSelect subquery) {
    super(value, "in", subquery);
  }

}
