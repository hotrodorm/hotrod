package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.expressions.numeric.BuiltInNumericFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class Extract extends BuiltInNumericFunction {

  private GeneralDateTimeExpression datetime;
  private DateTimeFieldExpression field;

  public Extract(final GeneralDateTimeExpression datetime, final DateTimeFieldExpression field) {
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
