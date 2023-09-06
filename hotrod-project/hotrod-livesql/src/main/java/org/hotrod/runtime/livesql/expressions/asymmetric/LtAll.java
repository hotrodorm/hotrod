package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class LtAll extends AsymmetricOperator {

  public LtAll(final Expression value, final Select<?> subquery) {
    super(value, "< all", subquery);
  }

}
