package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class EnclosedObjectExpression extends ObjectExpression {

  // Properties

  private ObjectExpression expr;

  // Constructor

  public EnclosedObjectExpression(final ObjectExpression expr) {
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
