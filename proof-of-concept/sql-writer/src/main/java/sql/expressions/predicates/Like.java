package sql.expressions.predicates;

import sql.expressions.Expression;

public class Like extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public Like(final Expression a, final Expression b) {
    super(a, "like", b, PRECEDENCE);
  }

}
