package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class CurrentDateTime extends BuiltInDateTimeFunction {

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().currentDateTime(w);
  }

}
