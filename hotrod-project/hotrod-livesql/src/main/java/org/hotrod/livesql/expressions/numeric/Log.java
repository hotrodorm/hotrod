package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Log extends BuiltInNumericFunction {

  private GeneralNumericExpression value;
  private GeneralNumericExpression base;

  public Log(final GeneralNumericExpression value, final GeneralNumericExpression base) {
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
