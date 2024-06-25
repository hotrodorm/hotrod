package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.numbers.BuiltInNumberFunction;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Extract extends BuiltInNumberFunction {

  private DateTimeExpression datetime;
  private DateTimeFieldExpression field;

  public Extract(final DateTimeExpression datetime, final DateTimeFieldExpression field) {
    super();
    this.datetime = datetime;
    this.field = field;
    super.register(this.datetime);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().extract(w, this.datetime, this.field);
  }

}
