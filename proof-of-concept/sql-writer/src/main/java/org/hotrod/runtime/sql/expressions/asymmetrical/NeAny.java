package org.hotrod.runtime.sql.expressions.asymmetrical;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class NeAny extends AsymmetricalOperator {

  public NeAny(final Expression value, final ExecutableSelect subquery) {
    super(value, "<> any", subquery);
  }

}
