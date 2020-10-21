package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Date extends BuiltInDateTimeFunction {

  private Expression<java.util.Date> dateTime;

  public Date(final Expression<java.util.Date> dateTime) {
    super();
    this.dateTime = dateTime;
    super.register(this.dateTime);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().date(w, this.dateTime);
  }

}
