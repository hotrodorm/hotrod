package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Power extends BuiltInNumericFunction {

  private NumericExpression value;
  private NumericExpression exponent;

  public Power(final NumericExpression value, final NumericExpression exponent) {
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
