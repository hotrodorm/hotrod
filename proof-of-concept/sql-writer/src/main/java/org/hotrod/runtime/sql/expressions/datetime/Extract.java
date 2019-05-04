package org.hotrod.runtime.sql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Extract extends DateTimeFunction {

  private Expression<Date> datetime;
  private Expression<String> field;

  public Extract(final Expression<Date> datetime, final Expression<String> field) {
    super();
    this.datetime = datetime;
    this.field = field;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().extract(w, this.datetime, this.field);
  }

}
