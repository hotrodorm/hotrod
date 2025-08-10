package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class Remainder extends BinaryNumericExpression<NumericExpression> {

  private NumericExpression a;
  private NumericExpression b;

  public Remainder(final NumericExpression a, final NumericExpression b) {
    super(a, "%", b, Expression.PRECEDENCE_MULT_DIV_MOD);
    this.a = a;
    this.b = b;
    super.register(this.a);
    super.register(this.b);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().remainder(w, this.a, this.b);
  }

}
