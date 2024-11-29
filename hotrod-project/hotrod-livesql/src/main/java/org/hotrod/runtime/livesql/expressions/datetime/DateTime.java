package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class DateTime extends BuiltInDateTimeFunction {

  private GeneralDateTimeExpression date;
  private GeneralDateTimeExpression time;

  public DateTime(final GeneralDateTimeExpression date, final GeneralDateTimeExpression time) {
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
