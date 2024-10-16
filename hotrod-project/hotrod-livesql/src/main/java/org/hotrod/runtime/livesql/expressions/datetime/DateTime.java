package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class DateTime extends BuiltInDateTimeFunction {

  private DateTimeExpression date;
  private DateTimeExpression time;

  public DateTime(final DateTimeExpression date, final DateTimeExpression time) {
    super();
    this.date = date;
    this.time = time;
    super.register(this.date);
    super.register(this.time);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().dateTime(w, this.date, this.time);
  }

}
