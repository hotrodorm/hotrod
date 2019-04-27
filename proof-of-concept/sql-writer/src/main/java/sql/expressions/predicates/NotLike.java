package sql.expressions.predicates;

import sql.expressions.Expression;

public class NotLike extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public NotLike(final Expression a, final Expression b) {
    super(a, "not like", b, PRECEDENCE);
  }

}
