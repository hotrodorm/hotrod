package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class EnclosedNumberExpression extends NumberExpression {

  // Properties

  private NumberExpression expr;

  // Constructor

  public EnclosedNumberExpression(final NumberExpression expr) {
    super(Expression.PRECEDENCE_PARENTHESIS);
    if (expr == null) {
      throw new LiveSQLException("Enclosed expression cannot be null");
    }
    this.expr = expr;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("(");
    Helper.renderTo(this.expr, w);
    w.write(")");
  }

}
