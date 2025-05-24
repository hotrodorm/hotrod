package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.queries.QueryWriter;

public class Abs extends BuiltInNumberFunction {

  private GeneralNumberExpression value;

  public Abs(final GeneralNumberExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().abs(w, this.value);
  }

}
