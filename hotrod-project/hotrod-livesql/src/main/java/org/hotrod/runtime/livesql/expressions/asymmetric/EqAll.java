package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class EqAll extends AsymmetricOperator {

  public EqAll(final ComparableExpression value, final Select<?> subquery) {
    super(value, "= all", subquery);
  }

}
