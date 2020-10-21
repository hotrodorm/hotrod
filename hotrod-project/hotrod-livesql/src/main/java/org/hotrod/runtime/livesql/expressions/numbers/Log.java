package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Log extends BuiltInNumberFunction {

  private Expression<Number> value;
  private Expression<Number> base;

  public Log(final Expression<Number> value, final Expression<Number> base) {
    super();
    this.value = value;
    this.base = base;
    super.register(this.value);
    super.register(this.base);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().logarithm(w, this.value, this.base);
  }

}
