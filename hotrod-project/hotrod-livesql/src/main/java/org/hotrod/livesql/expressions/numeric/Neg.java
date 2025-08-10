package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class Neg extends NumericSyntaxExpression {

  private NumericExpression value;

  public Neg(final NumericExpression value) {
    super(Expression.PRECEDENCE_UNARY_MINUS);
    this.value = value;
    super.register(this.value);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().neg(w, this.value);
  }

}
