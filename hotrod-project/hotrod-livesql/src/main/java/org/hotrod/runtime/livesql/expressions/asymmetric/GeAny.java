package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class GeAny extends AsymmetricOperator {

  public GeAny(final ComparableExpression value, final Select<?> subquery) {
    super(value, ">= any", subquery);
  }

}
