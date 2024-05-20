package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Date extends BuiltInDateTimeFunction {

  private DateTimeExpression dateTime;

  public Date(final DateTimeExpression dateTime) {
    super();
    this.dateTime = dateTime;
    super.register(this.dateTime);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().date(w, this.dateTime);
  }

}
