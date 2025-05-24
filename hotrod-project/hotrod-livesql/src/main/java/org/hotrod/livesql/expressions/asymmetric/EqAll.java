package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.queries.select.Select;

public class EqAll extends AsymmetricOperator {

  public EqAll(final EquatableExpression value, final Select<?> subquery) {
    super(value, "= all", subquery);
  }

}
