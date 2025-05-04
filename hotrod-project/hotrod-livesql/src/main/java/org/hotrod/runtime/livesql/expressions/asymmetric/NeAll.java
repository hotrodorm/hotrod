package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.EquatableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class NeAll extends AsymmetricOperator {

  public NeAll(final EquatableExpression value, final Select<?> subquery) {
    super(value, "<> all", subquery);
  }

}
