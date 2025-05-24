package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.Select;

public class GtAny extends AsymmetricOperator {

  public GtAny(final ComparableExpression value, final Select<?> subquery) {
    super(value, "> any", subquery);
  }

}
