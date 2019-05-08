package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Neg extends NumberExpression {

  private static final int PRECEDENCE = 2;

  private Expression<Number> value;

  public Neg(final Expression<Number> value) {
    super(PRECEDENCE);
    this.value = value;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().neg(w, this.value);
  }

}
