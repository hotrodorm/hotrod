package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.EquatableExpression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class InSubquery extends AsymmetricOperator {

  public InSubquery(final EquatableExpression value, final Select<?> subquery) {
    super(value, "in", subquery);
  }

}
