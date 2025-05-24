package org.hotrod.livesql.expressions.predicates;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class Not extends Predicate {

  private GeneralBooleanExpression a;

  public Not(final GeneralBooleanExpression a) {
    super(Expression.PRECEDENCE_NOT);
    this.a = a;
    super.register(this.a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("not ");
    super.renderInner(this.a, w);
  }

}
