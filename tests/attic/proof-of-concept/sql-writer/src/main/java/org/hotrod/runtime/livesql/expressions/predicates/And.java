package org.hotrod.runtime.livesql.expressions.predicates;

public class And extends BinaryPredicate {

  private static final int PRECEDENCE = 11;

  public And(final Predicate a, final Predicate b) {
    super(a, "and", b, PRECEDENCE);
  }

}
