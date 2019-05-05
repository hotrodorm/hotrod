package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Trunc extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> places;

  public Trunc(final Expression<Number> value, final Expression<Number> places) {
    super();
    this.value = value;
    this.places = places;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().trunc(w, this.value, this.places);
  }

}
