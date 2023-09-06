package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class LeAny extends AsymmetricOperator {

  public LeAny(final Expression value, final Select<?> subquery) {
    super(value, "<= any", subquery);
  }

}
