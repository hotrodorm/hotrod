package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class GeAny extends AsymmetricOperator {

  public GeAny(final Expression value, final ExecutableSelect<?> subquery) {
    super(value, ">= any", subquery);
  }

}
