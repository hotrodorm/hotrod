package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class NotLike extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  private Expression<String> escape;

  public NotLike(final Expression<String> a, final Expression<String> b) {
    super(a, "not like", b, PRECEDENCE);
    this.escape = null;
  }

  public NotLike(final Expression<String> a, final Expression<String> b, final Expression<String> escape) {
    super(a, "not like", b, PRECEDENCE);
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
