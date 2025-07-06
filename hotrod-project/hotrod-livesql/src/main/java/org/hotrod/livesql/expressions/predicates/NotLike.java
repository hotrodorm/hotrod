package org.hotrod.livesql.expressions.predicates;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class NotLike extends BinaryPredicate {

  private GeneralStringExpression escape;

  public NotLike(final GeneralStringExpression a, final GeneralStringExpression b) {
    super(a, "not like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public NotLike(final GeneralStringExpression a, final GeneralStringExpression b, final GeneralStringExpression escape) {
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
