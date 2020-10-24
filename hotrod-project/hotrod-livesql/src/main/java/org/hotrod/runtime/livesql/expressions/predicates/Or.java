package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Or extends BinaryPredicate {

  public Or(final Predicate a, final Predicate b) {
    super(a, "or", b, Expression.PRECEDENCE_OR);
  }

}
