package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class Neg extends NumericExpression {

  private GeneralNumericExpression value;

  public Neg(final GeneralNumericExpression value) {
    super(Expression.PRECEDENCE_UNARY_MINUS);
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().neg(w, this.value);
  }

}
