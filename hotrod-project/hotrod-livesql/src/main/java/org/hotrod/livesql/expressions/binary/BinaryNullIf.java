package org.hotrod.livesql.expressions.binary;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class BinaryNullIf extends BinarySyntaxExpression {

  private BinaryExpression a;
  private BinaryExpression b;

  public BinaryNullIf(final BinaryExpression a, final BinaryExpression b) {
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
