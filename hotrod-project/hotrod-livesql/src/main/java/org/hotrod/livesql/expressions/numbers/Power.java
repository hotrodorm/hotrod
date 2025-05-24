package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.queries.QueryWriter;

public class Power extends BuiltInNumberFunction {

  private GeneralNumberExpression value;
  private GeneralNumberExpression exponent;

  public Power(final GeneralNumberExpression value, final GeneralNumberExpression exponent) {
    super();
    this.value = value;
    this.exponent = exponent;
    super.register(this.value);
    super.register(this.exponent);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().power(w, this.value, this.exponent);
  }

}
