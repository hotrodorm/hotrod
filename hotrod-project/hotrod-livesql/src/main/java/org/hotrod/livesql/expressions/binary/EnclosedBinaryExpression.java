package org.hotrod.livesql.expressions.binary;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;

public class EnclosedBinaryExpression extends BinaryExpression {

  // Properties

  private GeneralBinaryExpression expr;

  // Constructor

  public EnclosedBinaryExpression(final GeneralBinaryExpression expr) {
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
    Shield.renderTo(this.expr, w);
    w.write(")");
  }

}
