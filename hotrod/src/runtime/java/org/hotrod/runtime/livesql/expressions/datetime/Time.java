package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class Time extends DateTimeFunction {

  private Expression<java.util.Date> timestamp;

  public Time(final Expression<Date> timestamp) {
    super();
    this.timestamp = timestamp;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().time(w, this.timestamp);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.timestamp.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.timestamp.designateAliases(ag);
  }

}