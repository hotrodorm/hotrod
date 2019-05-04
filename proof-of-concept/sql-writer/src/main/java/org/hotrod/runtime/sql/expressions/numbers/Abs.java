package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Abs extends NumericFunction {

  private Expression<Number> value;

  public Abs(final Expression<Number> value) {
    super();
    this.value = value;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().abs(w, this.value);
  }

}
