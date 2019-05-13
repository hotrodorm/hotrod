package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

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

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.value.gatherAliases(ag);
    this.places.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.places.designateAliases(ag);
  }

}
