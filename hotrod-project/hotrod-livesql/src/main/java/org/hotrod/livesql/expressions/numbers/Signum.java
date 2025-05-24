package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.queries.QueryWriter;

public class Signum extends BuiltInNumberFunction {

  private GeneralNumberExpression value;

  public Signum(final GeneralNumberExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().signum(w, this.value);
  }

}
