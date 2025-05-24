package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.queries.select.Select;

public class NotInSubquery extends AsymmetricOperator {

  public NotInSubquery(final EquatableExpression value, final Select<?> subquery) {
    super(value, "not in", subquery);
  }

}
