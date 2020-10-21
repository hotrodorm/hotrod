package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class DateTime extends BuiltInDateTimeFunction {

  private Expression<java.util.Date> date;
  private Expression<java.util.Date> time;

  public DateTime(final Expression<Date> date, final Expression<Date> time) {
    super();
    this.date = date;
    this.time = time;
    super.register(this.date);
    super.register(this.time);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().dateTime(w, this.date, this.time);
  }

}
