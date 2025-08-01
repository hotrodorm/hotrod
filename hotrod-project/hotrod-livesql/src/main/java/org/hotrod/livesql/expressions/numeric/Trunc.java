package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Trunc extends BuiltInNumericFunction {

  private GeneralNumericExpression value;
  private GeneralNumericExpression places;

  public Trunc(final GeneralNumericExpression value, final GeneralNumericExpression places) {
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
