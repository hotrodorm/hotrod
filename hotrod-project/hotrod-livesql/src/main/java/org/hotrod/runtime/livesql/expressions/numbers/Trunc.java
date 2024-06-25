package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Trunc extends BuiltInNumberFunction {

  private NumberExpression value;
  private NumberExpression places;

  public Trunc(final NumberExpression value, final NumberExpression places) {
    super();
    this.value = value;
    this.places = places;
    super.register(this.value);
    super.register(this.places);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().trunc(w, this.value, this.places);
  }

}
