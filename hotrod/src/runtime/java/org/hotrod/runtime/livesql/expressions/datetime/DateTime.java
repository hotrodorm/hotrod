package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

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

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.date.gatherAliases(ag);
    this.time.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.date.designateAliases(ag);
    this.time.designateAliases(ag);
  }

}
