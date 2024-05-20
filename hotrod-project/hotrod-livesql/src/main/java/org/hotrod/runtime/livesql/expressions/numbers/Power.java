package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Power extends BuiltInNumberFunction {

  private NumberExpression value;
  private NumberExpression exponent;

  public Power(final NumberExpression value, final NumberExpression exponent) {
    super();
    this.value = value;
    this.exponent = exponent;
    super.register(this.value);
    super.register(this.exponent);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().power(w, this.value, this.exponent);
  }

}
