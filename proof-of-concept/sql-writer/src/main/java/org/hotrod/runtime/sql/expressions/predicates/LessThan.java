package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.expressions.Expression;

public class LessThan extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public <T> LessThan(final Expression<T> a, final Expression<T> b) {
    super(a, "<", b, PRECEDENCE);
  }

}
