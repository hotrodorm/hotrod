package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Extract extends DateTimeFunction {

  private Expression<Date> datetime;
  private DateTimeFieldExpression field;

  public Extract(final Expression<Date> datetime, final DateTimeFieldExpression field) {
    super();
    this.datetime = datetime;
    this.field = field;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().extract(w, this.datetime, this.field);
  }

}
