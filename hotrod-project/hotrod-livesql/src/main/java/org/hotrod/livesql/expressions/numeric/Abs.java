package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Abs extends BuiltInNumericFunction {

  private GeneralNumericExpression value;

  public Abs(final GeneralNumericExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().abs(w, this.value);
  }

}
