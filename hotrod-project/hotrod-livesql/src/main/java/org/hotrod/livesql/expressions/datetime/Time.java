package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.queries.QueryWriter;

public class Time extends BuiltInDateTimeFunction {

  private GeneralDateTimeExpression timestamp;

  public Time(final GeneralDateTimeExpression timestamp) {
    super();
    this.timestamp = timestamp;
    super.register(this.timestamp);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().time(w, this.timestamp);
  }

}
