package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;

public class Neg extends NumberExpression {

  private static final int PRECEDENCE = 2;

  private NumberExpression a;

  protected Neg(final NumberExpression a) {
    super(PRECEDENCE);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("-");
    super.renderInner(this.a, w);
  }

}
