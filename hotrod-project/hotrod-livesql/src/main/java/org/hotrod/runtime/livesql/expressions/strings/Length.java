package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Length extends NumberExpression {

  private StringExpression string;

  public Length(final StringExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().length(w, this.string);
  }

}
