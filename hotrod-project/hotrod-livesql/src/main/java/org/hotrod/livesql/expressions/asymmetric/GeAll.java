package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.Select;

public class GeAll extends AsymmetricOperator {

  public GeAll(final ComparableExpression value, final Select<?> subquery) {
    super(value, ">= all", subquery);
  }

}
