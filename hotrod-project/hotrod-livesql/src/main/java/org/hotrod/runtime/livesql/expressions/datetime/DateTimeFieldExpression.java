package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class DateTimeFieldExpression extends Expression {

  private DateTimeField field;

  public DateTimeFieldExpression(final DateTimeField field) {
    super(Expression.PRECEDENCE_LITERAL);
    this.field = field;
  }

  @Override
  public void renderTo(final QueryWriter w) {
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
