package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NotLike extends BinaryPredicate {

  private Expression<String> escape;

  public NotLike(final Expression<String> a, final Expression<String> b) {
    super(a, "not like", b, Expression.PRECEDENCE_LIKE);
    this.escape = null;
  }

  public NotLike(final Expression<String> a, final Expression<String> b, final Expression<String> escape) {
    super(a, "not like", b, Expression.PRECEDENCE_LIKE);
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
