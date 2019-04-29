package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.expressions.Expression;

public class Equal extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public Equal(final Expression a, final Expression b) {
    super(a, "=", b, PRECEDENCE);
  }

}
