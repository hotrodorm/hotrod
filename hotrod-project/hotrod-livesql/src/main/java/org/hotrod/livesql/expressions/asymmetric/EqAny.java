package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.queries.select.Select;

public class EqAny extends AsymmetricOperator {

  public EqAny(final EquatableExpression value, final Select<?> subquery) {
    super(value, "= any", subquery);
  }

}
