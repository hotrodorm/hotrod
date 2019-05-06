package org.hotrod.runtime.sql.expressions.asymmetric;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class LtAny extends AsymmetricOperator {

  public LtAny(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "< any", subquery);
  }

}
