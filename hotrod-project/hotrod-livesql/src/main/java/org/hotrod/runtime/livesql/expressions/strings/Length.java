package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFreeExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Length extends NumberFreeExpression {

  private StringExpression string;

  public Length(final StringExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().length(w, this.string);
  }

}
