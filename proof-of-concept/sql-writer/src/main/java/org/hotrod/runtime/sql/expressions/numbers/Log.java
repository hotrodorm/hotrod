package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Log extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> base;

  public Log(final Expression<Number> value, final Expression<Number> base) {
    super();
    this.value = value;
    this.base = base;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().logarithm(w, this.value, this.base);
  }

}
