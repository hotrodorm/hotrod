package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Neg extends NumberExpression {

  private static final int PRECEDENCE = 2;

  private Expression<Number> a;

  public Neg(final Expression<Number> a) {
    super(PRECEDENCE);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("-");
    super.renderInner(this.a, w);
  }

}
