package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

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

}
