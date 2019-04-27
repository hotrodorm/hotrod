package sql.expressions.predicates;

import sql.expressions.Expression;

public class LessThan extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public LessThan(final Expression a, final Expression b) {
    super(a, "<", b, PRECEDENCE);
  }

}
