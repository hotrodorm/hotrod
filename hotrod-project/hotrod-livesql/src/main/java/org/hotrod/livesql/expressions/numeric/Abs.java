package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Abs extends BuiltInNumericFunction {

  private NumericExpression value;

  public Abs(final NumericExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().abs(w, this.value);
  }

}
