package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.expressions.Expression;

public class In extends AsymmetricOperator {

  public In(final Expression<?> value, final ExecutableSelect subquery) {
    super(value, "in", subquery);
  }

}
