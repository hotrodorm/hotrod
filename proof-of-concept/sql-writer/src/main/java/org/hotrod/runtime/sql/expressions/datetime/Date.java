package org.hotrod.runtime.sql.expressions.datetime;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

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

}
