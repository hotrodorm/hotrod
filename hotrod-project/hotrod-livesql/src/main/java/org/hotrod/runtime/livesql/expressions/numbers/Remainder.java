package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Remainder extends BinaryNumberExpression<NumberExpression> {

  private NumberExpression a;
  private NumberExpression b;

  public Remainder(final NumberExpression a, final NumberExpression b) {
    super(a, "%", b, Expression.PRECEDENCE_MULT_DIV_MOD);
    this.a = a;
    this.b = b;
    super.register(this.a);
    super.register(this.b);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().remainder(w, this.a, this.b);
  }

}
