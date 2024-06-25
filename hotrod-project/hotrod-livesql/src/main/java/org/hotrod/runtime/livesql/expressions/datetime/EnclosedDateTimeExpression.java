package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class EnclosedDateTimeExpression extends DateTimeExpression {

  // Properties

  private DateTimeExpression expr;

  // Constructor

  public EnclosedDateTimeExpression(final DateTimeExpression expr) {
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
