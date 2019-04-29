package org.hotrod.runtime.sql.expressions.asymmetrical;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.expressions.Expression;

public class LtAll extends AsymmetricalOperator {

  public LtAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "< all", subquery);
  }

}
