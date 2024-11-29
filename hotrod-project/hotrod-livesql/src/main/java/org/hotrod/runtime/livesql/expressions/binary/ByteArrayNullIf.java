package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class ByteArrayNullIf extends ByteArrayExpression {

  private GeneralByteArrayExpression a;
  private GeneralByteArrayExpression b;

  public ByteArrayNullIf(final GeneralByteArrayExpression a, final GeneralByteArrayExpression b) {
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
