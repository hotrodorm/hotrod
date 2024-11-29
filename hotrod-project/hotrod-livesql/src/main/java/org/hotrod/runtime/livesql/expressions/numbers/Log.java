package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Log extends BuiltInNumberFunction {

  private GeneralNumberExpression value;
  private GeneralNumberExpression base;

  public Log(final GeneralNumberExpression value, final GeneralNumberExpression base) {
    super();
    this.value = value;
    this.base = base;
    super.register(this.value);
    super.register(this.base);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().logarithm(w, this.value, this.base);
  }

}
