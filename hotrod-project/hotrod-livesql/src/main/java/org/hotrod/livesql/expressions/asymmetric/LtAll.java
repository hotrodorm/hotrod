package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.Select;

public class LtAll extends AsymmetricOperator {

  public LtAll(final ComparableExpression value, final Select<?> subquery) {
    super(value, "< all", subquery);
  }

}
