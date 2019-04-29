package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Like extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  private Expression escape;

  public Like(final Expression a, final Expression b) {
    super(a, "like", b, PRECEDENCE);
    this.escape = null;
  }

  public Like(final Expression a, final Expression b, final Expression escape) {
    super(a, "like", b, PRECEDENCE);
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
