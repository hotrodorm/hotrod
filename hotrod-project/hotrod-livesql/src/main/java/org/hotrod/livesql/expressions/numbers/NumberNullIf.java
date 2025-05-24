package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class NumberNullIf extends NumberExpression {

  private GeneralNumberExpression a;
  private GeneralNumberExpression b;

  public NumberNullIf(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.a = a;
    this.b = b;
    super.register(a);
    super.register(b);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().nullif(w, this.a, this.b);
  }

}
