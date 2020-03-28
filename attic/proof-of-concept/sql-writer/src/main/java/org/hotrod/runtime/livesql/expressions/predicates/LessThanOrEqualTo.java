package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public class LessThanOrEqualTo extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public <T> LessThanOrEqualTo(final Expression<T> a, final Expression<T> b) {
    super(a, "<=", b, PRECEDENCE);
  }

}
