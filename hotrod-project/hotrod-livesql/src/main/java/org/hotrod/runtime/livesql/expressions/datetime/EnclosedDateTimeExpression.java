package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class EnclosedDateTimeExpression extends DateTimeExpression {

  // Properties

  private DateTimeExpression expr;

  // Constructor

  public EnclosedDateTimeExpression(final DateTimeExpression expr) {
    super(Expression.PRECEDENCE_PARENTHESIS);
    this.expr = expr;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("(");
    this.expr.renderTo(w);
    w.write(")");
  }

}