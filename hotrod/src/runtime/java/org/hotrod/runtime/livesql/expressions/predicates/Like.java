package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Like extends BinaryPredicate {

  private Expression<String> escape;

  public Like(final Expression<String> a, final Expression<String> b) {
    super(a, "like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public Like(final Expression<String> a, final Expression<String> b, final Expression<String> escape) {
    super(a, "like", b, PRECEDENCE_LIKE);
    this.escape = escape;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderTo(w);
    if (this.escape != null) {
      w.write(" escape '");
      w.write(this.escape);
      w.write("'");
    }
  }

}
