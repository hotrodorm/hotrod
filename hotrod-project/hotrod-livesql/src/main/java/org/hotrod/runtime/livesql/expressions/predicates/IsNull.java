package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class IsNull extends Predicate {

  private Expression a;

  public IsNull(final Expression a) {
    super(Expression.PRECEDENCE_IS_NULL);
    this.a = a;
    super.register(this.a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    super.renderInner(this.a, w);
    w.write(" is null");
  }

}
