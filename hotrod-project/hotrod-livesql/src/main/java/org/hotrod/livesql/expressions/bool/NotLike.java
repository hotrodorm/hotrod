package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class NotLike extends BinaryPredicate {

  private GeneralCharExpression escape;

  public NotLike(final GeneralCharExpression a, final GeneralCharExpression b) {
    super(a, "not like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public NotLike(final GeneralCharExpression a, final GeneralCharExpression b, final GeneralCharExpression escape) {
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
