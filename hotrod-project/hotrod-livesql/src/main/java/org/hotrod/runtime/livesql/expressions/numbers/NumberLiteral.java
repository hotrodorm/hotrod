package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class NumberLiteral extends NumberExpression {

  // Properties

  protected String formatted;

  protected NumberLiteral(int precedence) {
    super(precedence);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(this.formatted);
  }

}
