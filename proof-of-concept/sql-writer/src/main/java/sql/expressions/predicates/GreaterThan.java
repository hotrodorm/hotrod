package sql.expressions.predicates;

import sql.expressions.Expression;

public class GreaterThan extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public GreaterThan(final Expression a, final Expression b) {
    super(a, ">", b, PRECEDENCE);
  }

}
