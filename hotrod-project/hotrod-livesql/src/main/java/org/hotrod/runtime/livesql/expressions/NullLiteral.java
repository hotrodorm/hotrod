package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class NullLiteral extends GenericExpression {

  public NullLiteral() {
    super(Expression.PRECEDENCE_LITERAL);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("NULL");
  }

}
