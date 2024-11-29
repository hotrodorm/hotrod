package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Round extends BuiltInNumberFunction {

  private GeneralNumberExpression value;
  private GeneralNumberExpression places;

  public Round(final GeneralNumberExpression value, final GeneralNumberExpression places) {
    super();
    this.value = value;
    this.places = places;
    super.register(this.value);
    super.register(this.places);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().round(w, this.value, this.places);
  }

}
