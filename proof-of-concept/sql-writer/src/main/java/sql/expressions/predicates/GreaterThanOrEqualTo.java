package sql.expressions.predicates;

import sql.expressions.Expression;

public class GreaterThanOrEqualTo extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public GreaterThanOrEqualTo(final Expression a, final Expression b) {
    super(a, ">=", b, PRECEDENCE);
  }

}
