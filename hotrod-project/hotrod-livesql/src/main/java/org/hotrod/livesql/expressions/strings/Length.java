package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Length extends NumberExpression {

  private GeneralStringExpression string;

  public Length(final GeneralStringExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().length(w, this.string);
  }

}
