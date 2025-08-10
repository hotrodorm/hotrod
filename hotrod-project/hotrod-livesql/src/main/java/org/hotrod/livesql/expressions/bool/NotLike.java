package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class NotLike extends BinaryPredicate {

  private CharExpression escape;

  public NotLike(final CharExpression a, final CharExpression b) {
    super(a, "not like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public NotLike(final CharExpression a, final CharExpression b, final CharExpression escape) {
    super(a, "not like", b, Expression.PRECEDENCE_LIKE);
    this.escape = escape;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    super.renderTo(w);
    if (this.escape != null) {
      w.write(" escape '");
      Shield.renderTo(this.escape, w);
      w.write("'");
    }
  }

}
