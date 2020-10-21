package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Power extends BuiltInNumberFunction {

  private Expression<Number> value;
  private Expression<Number> exponent;

  public Power(final Expression<Number> value, final Expression<Number> exponent) {
    super();
    this.value = value;
    this.exponent = exponent;
    super.register(this.value);
    super.register(this.exponent);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().power(w, this.value, this.exponent);
  }

}
