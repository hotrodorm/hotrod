package org.hotrod.runtime.sql.expressions.asymmetric;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class GtAll extends AsymmetricOperator {

  public GtAll(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "> all", subquery);
  }

}
