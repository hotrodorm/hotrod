package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.EquatableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class EqAny extends AsymmetricOperator {

  public EqAny(final EquatableExpression value, final Select<?> subquery) {
    super(value, "= any", subquery);
  }

}
