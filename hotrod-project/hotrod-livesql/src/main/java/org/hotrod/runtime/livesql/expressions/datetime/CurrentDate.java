package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class CurrentDate extends BuiltInDateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().currentDate(w);
  }

}
