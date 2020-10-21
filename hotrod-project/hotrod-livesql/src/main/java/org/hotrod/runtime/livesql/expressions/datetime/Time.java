package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Time extends BuiltInDateTimeFunction {

  private Expression<java.util.Date> timestamp;

  public Time(final Expression<Date> timestamp) {
    super();
    this.timestamp = timestamp;
    super.register(this.timestamp);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().time(w, this.timestamp);
  }

}
