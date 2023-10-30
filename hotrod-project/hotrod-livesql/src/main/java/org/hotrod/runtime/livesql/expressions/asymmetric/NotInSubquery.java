package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class NotInSubquery extends AsymmetricOperator {

  public NotInSubquery(final ComparableExpression value, final Select<?> subquery) {
    super(value, "not in", subquery);
  }

}
