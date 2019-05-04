package org.hotrod.runtime.sql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

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

}
