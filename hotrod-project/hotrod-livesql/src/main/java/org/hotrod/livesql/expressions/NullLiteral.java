package org.hotrod.livesql.expressions;

import org.hotrod.livesql.queries.QueryWriter;

public class NullLiteral extends ExistenceExpression {

  public NullLiteral() {
    super(Expression.PRECEDENCE_LITERAL);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("NULL");
  }

}
