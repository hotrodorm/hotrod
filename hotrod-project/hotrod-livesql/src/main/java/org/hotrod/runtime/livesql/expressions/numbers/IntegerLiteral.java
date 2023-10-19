package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class IntegerLiteral extends NumberExpression {

  // Properties

  private long value;

  // Constructor

  public IntegerLiteral(final long value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("" + this.value);
  }

}
