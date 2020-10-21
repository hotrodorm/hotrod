package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Signum extends BuiltInNumberFunction {

  private Expression<Number> value;

  public Signum(final Expression<Number> value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().signum(w, this.value);
  }

}
