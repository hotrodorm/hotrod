package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.value.validateTableReferences(tableReferences, ag);
    this.places.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.places.designateAliases(ag);
  }

}