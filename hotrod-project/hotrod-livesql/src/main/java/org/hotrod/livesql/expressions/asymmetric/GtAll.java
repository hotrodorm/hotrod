package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.Select;

public class GtAll extends AsymmetricOperator {

  public GtAll(final ComparableExpression value, final Select<?> subquery) {
    super(value, "> all", subquery);
  }

}
