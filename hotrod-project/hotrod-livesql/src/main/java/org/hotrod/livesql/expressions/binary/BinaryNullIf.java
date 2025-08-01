package org.hotrod.livesql.expressions.binary;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class BinaryNullIf extends BinaryExpression {

  private GeneralBinaryExpression a;
  private GeneralBinaryExpression b;

  public BinaryNullIf(final GeneralBinaryExpression a, final GeneralBinaryExpression b) {
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
