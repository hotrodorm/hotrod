package org.hotrod.livesql.expressions.datetime.literals;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.datetime.DateTimeSyntaxExpression;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class DateTimeLiteral extends DateTimeSyntaxExpression {

  // Properties

  protected String formatted;

  // Constructor

  public DateTimeLiteral() {
    super(Expression.PRECEDENCE_LITERAL);
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write(this.formatted);
  }

}
