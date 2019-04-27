package sql.expressions.predicates;

import sql.expressions.Expression;

public abstract class Predicate extends Expression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

}
