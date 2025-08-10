package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class Not extends BooleanSyntaxExpression {

  private BooleanExpression a;

  public Not(final BooleanExpression a) {
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
