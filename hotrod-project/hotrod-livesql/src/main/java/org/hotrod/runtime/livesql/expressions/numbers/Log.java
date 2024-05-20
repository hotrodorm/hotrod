package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Log extends BuiltInNumberFunction {

  private NumberExpression value;
  private NumberExpression base;

  public Log(final NumberExpression value, final NumberExpression base) {
    super();
    this.value = value;
    this.base = base;
    super.register(this.value);
    super.register(this.base);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().logarithm(w, this.value, this.base);
  }

}
