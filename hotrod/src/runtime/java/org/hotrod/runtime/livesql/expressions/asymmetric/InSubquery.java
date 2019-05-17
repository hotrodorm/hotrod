package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class InSubquery extends AsymmetricOperator {

  public InSubquery(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "in", subquery);
  }

}
