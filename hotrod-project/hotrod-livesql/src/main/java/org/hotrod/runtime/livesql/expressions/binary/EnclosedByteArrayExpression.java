package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class EnclosedByteArrayExpression extends ByteArrayExpression {

  // Properties

  private ByteArrayExpression expr;

  // Constructor

  public EnclosedByteArrayExpression(final ByteArrayExpression expr) {
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
