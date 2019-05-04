package org.hotrod.runtime.sql.expressions.datetime;

import org.hotrod.runtime.sql.QueryWriter;

public class CurrentDate extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentDate(w);
  }

}
