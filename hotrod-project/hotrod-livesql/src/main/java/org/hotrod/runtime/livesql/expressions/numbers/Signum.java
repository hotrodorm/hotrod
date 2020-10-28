package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Signum extends BuiltInNumberFunction {

  private NumberExpression value;

  public Signum(final NumberExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().signum(w, this.value);
  }

}
