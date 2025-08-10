package org.hotrod.livesql.expressions.object;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class ObjectNullIf extends ObjectSyntaxExpression {

  private ObjectExpression a;
  private ObjectExpression b;

  public ObjectNullIf(final ObjectExpression a, final ObjectExpression b) {
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
