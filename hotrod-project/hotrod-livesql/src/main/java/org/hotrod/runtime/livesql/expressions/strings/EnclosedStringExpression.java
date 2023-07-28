package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class EnclosedStringExpression extends StringExpression {

  // Properties

  private StringExpression expr;

  // Constructor

  public EnclosedStringExpression(final StringExpression expr) {
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
