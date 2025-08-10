package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class CharNullIf extends CharSyntaxExpression {

  private CharExpression a;
  private CharExpression b;

  public CharNullIf(final CharExpression a, final CharExpression b) {
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
