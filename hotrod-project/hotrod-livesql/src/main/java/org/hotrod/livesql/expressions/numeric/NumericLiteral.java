package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.queries.QueryWriter;

public abstract class NumericLiteral extends NumericSyntaxExpression {

  // Properties

  protected String formatted;

  protected NumericLiteral(int precedence) {
    super(precedence);
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write(this.formatted);
  }

}
