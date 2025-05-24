package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.Select;

public class LeAny extends AsymmetricOperator {

  public LeAny(final ComparableExpression value, final Select<?> subquery) {
    super(value, "<= any", subquery);
  }

}
