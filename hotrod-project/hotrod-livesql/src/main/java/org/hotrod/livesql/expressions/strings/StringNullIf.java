package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class StringNullIf extends StringExpression {

  private GeneralStringExpression a;
  private GeneralStringExpression b;

  public StringNullIf(final GeneralStringExpression a, final GeneralStringExpression b) {
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
