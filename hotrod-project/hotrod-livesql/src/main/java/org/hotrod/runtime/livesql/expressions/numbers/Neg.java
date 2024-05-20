package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Neg extends NumberExpression {

  private NumberExpression value;

  public Neg(final NumberExpression value) {
    super(Expression.PRECEDENCE_UNARY_MINUS);
    this.value = value;
    super.register(this.value);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().neg(w, this.value);
  }

}
