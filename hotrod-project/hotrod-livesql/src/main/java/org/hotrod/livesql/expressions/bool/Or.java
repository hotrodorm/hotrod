package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class Or extends BinaryPredicate {

  public Or(final Predicate a, final Predicate b) {
    super(a, "or", b, Expression.PRECEDENCE_OR);
  }

}
