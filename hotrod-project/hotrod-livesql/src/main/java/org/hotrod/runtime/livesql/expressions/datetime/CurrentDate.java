package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class CurrentDate extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentDate(w);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    // nothing to do
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do
  }

}
