package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public class Signum extends BuiltInNumericFunction {

  private NumericExpression value;

  public Signum(final NumericExpression value) {
    super();
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().signum(w, this.value);
  }

}
