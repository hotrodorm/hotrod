package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.queries.select.Select;

public class NeAll extends AsymmetricOperator {

  public NeAll(final EquatableExpression value, final Select<?> subquery) {
    super(value, "<> all", subquery);
  }

}
