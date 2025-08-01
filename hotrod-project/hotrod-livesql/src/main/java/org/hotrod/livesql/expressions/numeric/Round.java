package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Round extends BuiltInNumericFunction {

  private GeneralNumericExpression value;
  private GeneralNumericExpression places;

  public Round(final GeneralNumericExpression value, final GeneralNumericExpression places) {
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
