package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class IsNotNull extends BooleanSyntaxExpression {

  private Expression a;

  public IsNotNull(final Expression a) {
    super(Expression.PRECEDENCE_IS_NULL);
    this.a = a;
    super.register(this.a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    super.renderInner(this.a, w);
    w.write(" is not null");
  }

}
