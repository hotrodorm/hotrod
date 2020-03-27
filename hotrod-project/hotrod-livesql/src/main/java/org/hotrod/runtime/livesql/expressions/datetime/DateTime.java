package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class DateTime extends DateTimeFunction {

  private Expression<java.util.Date> date;
  private Expression<java.util.Date> time;

  public DateTime(final Expression<Date> date, final Expression<Date> time) {
    super();
    this.date = date;
    this.time = time;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().dateTime(w, this.date, this.time);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.date.validateTableReferences(tableReferences, ag);
    this.time.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.date.designateAliases(ag);
    this.time.designateAliases(ag);
  }

}
