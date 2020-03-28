package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.QueryWriter;

public class CurrentDateTime extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentDateTime(w);
  }

}
