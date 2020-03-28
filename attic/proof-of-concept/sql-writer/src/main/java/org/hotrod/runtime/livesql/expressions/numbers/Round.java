package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Round extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> places;

  public Round(final Expression<Number> value, final Expression<Number> places) {
    super();
    this.value = value;
    this.places = places;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().round(w, this.value, this.places);
  }

}
