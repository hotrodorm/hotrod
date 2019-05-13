package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Date extends DateTimeFunction {

  private Expression<java.util.Date> dateTime;

  public Date(final Expression<java.util.Date> dateTime) {
    super();
    this.dateTime = dateTime;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().date(w, this.dateTime);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.dateTime.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.dateTime.designateAliases(ag);
  }

}
