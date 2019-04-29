package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.expressions.Expression;

public class GreaterThanOrEqualTo extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public GreaterThanOrEqualTo(final Expression a, final Expression b) {
    super(a, ">=", b, PRECEDENCE);
  }

}
