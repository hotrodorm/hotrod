package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;

public class EnclosedDateTimeExpression extends DateTimeSyntaxExpression {

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
    Shield.renderTo(this.expr, w);
    w.write(")");
  }

}
