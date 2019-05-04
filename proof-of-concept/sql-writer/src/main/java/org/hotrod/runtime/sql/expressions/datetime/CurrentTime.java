package org.hotrod.runtime.sql.expressions.datetime;

import org.hotrod.runtime.sql.QueryWriter;

public class CurrentTime extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentTime(w);
  }

}
