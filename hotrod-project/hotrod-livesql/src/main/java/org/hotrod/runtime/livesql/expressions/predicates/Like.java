package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Like extends BinaryPredicate {

  private StringExpression escape;

  public Like(final StringExpression a, final StringExpression b) {
    super(a, "like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public Like(final StringExpression a, final StringExpression b, final StringExpression escape) {
    super(a, "like", b, PRECEDENCE_LIKE);
    this.escape = escape;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderTo(w);
    if (this.escape != null) {
      w.write(" escape '");
      this.escape.renderTo(w);
      w.write("'");
    }
  }

}
