package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NullLiteral extends Expression {

  public static final NullLiteral NULL = new NullLiteral();

  protected NullLiteral() {
    super(Expression.PRECEDENCE_LITERAL);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("NULL");
  }

}
