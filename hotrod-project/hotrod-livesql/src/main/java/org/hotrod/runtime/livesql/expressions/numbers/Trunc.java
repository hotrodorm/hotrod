package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

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
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().trunc(w, this.value, this.places);
  }

}
