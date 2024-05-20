package org.hotrod.runtime.livesql.expressions.datetime.literals;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class DateTimeLiteral extends DateTimeExpression {

  // Properties

  protected String formatted;

  // Constructor

  public DateTimeLiteral() {
    super(Expression.PRECEDENCE_LITERAL);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(this.formatted);
  }

}
