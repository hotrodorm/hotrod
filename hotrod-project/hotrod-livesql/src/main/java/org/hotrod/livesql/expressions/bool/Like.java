package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Like extends BinaryPredicate {

  private CharExpression escape;

  public Like(final CharExpression a, final CharExpression b) {
    super(a, "like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public Like(final CharExpression a, final CharExpression b, final CharExpression escape) {
    super(a, "like", b, Expression.PRECEDENCE_LIKE);
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
