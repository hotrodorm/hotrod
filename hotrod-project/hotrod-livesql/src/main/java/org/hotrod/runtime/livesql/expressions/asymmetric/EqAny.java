package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class EqAny extends AsymmetricOperator {

  public EqAny(final ComparableExpression value, final Select<?> subquery) {
    super(value, "= any", subquery);
  }

}
