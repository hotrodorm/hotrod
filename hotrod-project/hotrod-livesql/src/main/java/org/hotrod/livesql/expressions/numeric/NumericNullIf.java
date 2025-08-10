package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class NumericNullIf extends NumericSyntaxExpression {

  private NumericExpression a;
  private NumericExpression b;

  public NumericNullIf(final NumericExpression a, final NumericExpression b) {
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
