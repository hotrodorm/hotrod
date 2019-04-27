package sql.expressions.predicates;

import sql.expressions.Expression;

public class LessThanOrEqualTo extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public LessThanOrEqualTo(final Expression a, final Expression b) {
    super(a, "<=", b, PRECEDENCE);
  }

}
