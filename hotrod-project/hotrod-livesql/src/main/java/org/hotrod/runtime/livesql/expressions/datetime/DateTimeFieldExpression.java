package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class DateTimeFieldExpression extends ComparableExpression {

  private DateTimeField field;

  public DateTimeFieldExpression(final DateTimeField field) {
    super(Expression.PRECEDENCE_LITERAL);
    this.field = field;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write(this.field.name().toLowerCase());
  }

  public static enum DateTimeField {
    YEAR, //
    MONTH, //
    DAY, //
    HOUR, //
    MINUTE, //
    SECOND, //
    TIMEZONE_HOUR, //
    TIMEZONE_MINUTE, //
    TZOFFSET, //
    QUARTER, //
    WEEK, //
    DOW, //
    MILLISECOND;
  }

}
