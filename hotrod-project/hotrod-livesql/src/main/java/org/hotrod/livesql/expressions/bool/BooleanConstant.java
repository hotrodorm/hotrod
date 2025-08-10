package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class BooleanConstant extends BooleanSyntaxExpression {

  // Properties

  private Boolean value;

  // Constructor

  public BooleanConstant(final Boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("" + this.value);
  }

}
