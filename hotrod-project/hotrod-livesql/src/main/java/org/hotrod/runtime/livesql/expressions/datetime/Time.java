package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Time extends BuiltInDateTimeFunction {

  private DateTimeExpression timestamp;

  public Time(final DateTimeExpression timestamp) {
    super();
    this.timestamp = timestamp;
    super.register(this.timestamp);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().time(w, this.timestamp);
  }

}
