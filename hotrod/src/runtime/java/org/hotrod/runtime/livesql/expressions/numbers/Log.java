package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class Log extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> base;

  public Log(final Expression<Number> value, final Expression<Number> base) {
    super();
    this.value = value;
    this.base = base;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().logarithm(w, this.value, this.base);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.value.validateTableReferences(tableReferences, ag);
    this.base.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.base.designateAliases(ag);
  }

}