package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class Remainder extends BinaryNumericExpression<GeneralNumericExpression> {

  private GeneralNumericExpression a;
  private GeneralNumericExpression b;

  public Remainder(final GeneralNumericExpression a, final GeneralNumericExpression b) {
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
