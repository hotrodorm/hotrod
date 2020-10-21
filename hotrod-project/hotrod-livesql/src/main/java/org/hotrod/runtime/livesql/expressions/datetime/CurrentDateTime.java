package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class CurrentDateTime extends BuiltInDateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentDateTime(w);
  }

}
