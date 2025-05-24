package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Helper;
import org.hotrod.livesql.queries.QueryWriter;

public class EnclosedStringExpression extends StringExpression {

  // Properties

  private GeneralStringExpression expr;

  // Constructor

  public EnclosedStringExpression(final GeneralStringExpression expr) {
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
