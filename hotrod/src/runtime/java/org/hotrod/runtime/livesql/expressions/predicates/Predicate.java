package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class Predicate extends Expression<Boolean> {

  protected Predicate(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public Predicate coalesce(final Predicate a) {
    return new PredicateCoalesce(this, a);
  }

  public Predicate coalesce(final Boolean a) {
    return new PredicateCoalesce(this, new BooleanConstant(a));
  }

  // Predicate operators

  public Predicate and(final Expression<Boolean> p) {
    return new And(this, p);
  }

  public Predicate andNot(final Expression<Boolean> p) {
    return new And(this, new Not(p));
  }

  public Predicate or(final Expression<Boolean> p) {
    return new Or(this, p);
  }

  public Predicate orNot(final Expression<Boolean> p) {
    return new Or(this, new Not(p));
  }

}