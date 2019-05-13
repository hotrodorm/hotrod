package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

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

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.timestamp.gatherAliases(ag);

  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.timestamp.designateAliases(ag);
  }

}
