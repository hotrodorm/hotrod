package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Round extends BuiltInNumericFunction {

  private NumericExpression value;
  private NumericExpression places;

  public Round(final NumericExpression value, final NumericExpression places) {
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
