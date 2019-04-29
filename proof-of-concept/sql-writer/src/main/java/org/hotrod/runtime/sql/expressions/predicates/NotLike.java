package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class NotLike extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  private Expression escape;

  public NotLike(final Expression a, final Expression b) {
    super(a, "not like", b, PRECEDENCE);
    this.escape = null;
  }

  public NotLike(final Expression a, final Expression b, final Expression escape) {
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
