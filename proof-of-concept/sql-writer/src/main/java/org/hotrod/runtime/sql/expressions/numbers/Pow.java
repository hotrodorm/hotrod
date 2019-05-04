package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Pow extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> exponent;

  public Pow(final Expression<Number> value, final Expression<Number> exponent) {
    super();
    this.value = value;
    this.exponent = exponent;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().power(w, this.value, this.exponent);
  }

}
