package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Not extends Predicate {

  private Predicate a;

  public Not(final Predicate a) {
    super(Expression.PRECEDENCE_NOT);
    this.a = a;
    super.register(this.a);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("not ");
    super.renderInner(this.a, w);
  }

}
