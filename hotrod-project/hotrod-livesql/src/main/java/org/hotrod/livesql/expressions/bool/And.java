package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class And extends BinaryPredicate {

  public And(final Predicate a, final Predicate b) {
    super(a, "and", b, Expression.PRECEDENCE_AND);
  }

}
