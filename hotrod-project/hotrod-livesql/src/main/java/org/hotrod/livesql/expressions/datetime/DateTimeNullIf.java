package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class DateTimeNullIf extends DateTimeExpression {

  private GeneralDateTimeExpression a;
  private GeneralDateTimeExpression b;

  public DateTimeNullIf(final GeneralDateTimeExpression a, final GeneralDateTimeExpression b) {
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
