package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.QueryWriter;

public class CurrentTime extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentTime(w);
  }

}
