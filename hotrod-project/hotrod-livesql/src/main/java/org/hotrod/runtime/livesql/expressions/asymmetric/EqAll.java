package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.EquatableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class EqAll extends AsymmetricOperator {

  public EqAll(final EquatableExpression value, final Select<?> subquery) {
    super(value, "= all", subquery);
  }

}
