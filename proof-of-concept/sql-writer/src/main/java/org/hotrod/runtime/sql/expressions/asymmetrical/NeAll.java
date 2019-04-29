package org.hotrod.runtime.sql.expressions.asymmetrical;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class NeAll extends AsymmetricalOperator {

  public NeAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "<> all", subquery);
  }

}
