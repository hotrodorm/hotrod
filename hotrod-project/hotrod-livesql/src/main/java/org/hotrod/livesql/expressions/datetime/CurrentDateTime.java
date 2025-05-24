package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.queries.QueryWriter;

public class CurrentDateTime extends BuiltInDateTimeFunction {

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().currentDateTime(w);
  }

}
