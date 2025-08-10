package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.queries.QueryWriter;

public class Date extends BuiltInDateTimeFunction {

  private DateTimeExpression dateTime;

  public Date(final DateTimeExpression dateTime) {
    super();
    this.dateTime = dateTime;
    super.register(this.dateTime);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().date(w, this.dateTime);
  }

}
