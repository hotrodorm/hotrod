package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.queries.select.Select;

public class InSubquery extends AsymmetricOperator {

  public InSubquery(final EquatableExpression value, final Select<?> subquery) {
    super(value, "in", subquery);
  }

}
