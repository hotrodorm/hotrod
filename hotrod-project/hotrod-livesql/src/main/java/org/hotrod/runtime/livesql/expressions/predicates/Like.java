package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Like extends BinaryPredicate {

  private GeneralStringExpression escape;

  public Like(final GeneralStringExpression a, final GeneralStringExpression b) {
    super(a, "like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public Like(final GeneralStringExpression a, final GeneralStringExpression b, final GeneralStringExpression escape) {
    super(a, "like", b, Expression.PRECEDENCE_LIKE);
    this.escape = escape;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    super.renderTo(w);
    if (this.escape != null) {
      w.write(" escape '");
      Helper.renderTo(this.escape, w);
      w.write("'");
    }
  }

}
