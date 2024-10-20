package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class CurrentDate extends BuiltInDateTimeFunction {

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().currentDate(w);
  }

}
