package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Abs extends BuiltInNumberFunction {

  private NumberExpression value;

  public Abs(final NumberExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().abs(w, this.value);
  }

}
