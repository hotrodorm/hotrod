package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Like extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  private Expression<String> escape;

  public Like(final Expression<String> a, final Expression<String> b) {
    super(a, "like", b, PRECEDENCE);
    this.escape = null;
  }

  public Like(final Expression<String> a, final Expression<String> b, final Expression<String> escape) {
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
