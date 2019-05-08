package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;

public class DateTimeFieldExpression extends Expression<DateTimeField> {

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

//  public static DateTimeFieldExpression from(final String s) {
//    for (DateTimeField f : DateTimeField.values()) {
//      if (f.name().equalsIgnoreCase(s)) {
//        return new DateTimeFieldExpression(f);
//      }
//    }
//    StringBuilder sb = new StringBuilder();
//    Separator sep = new Separator();
//    for (DateTimeField f : DateTimeField.values()) {
//      sb.append(sep.render());
//      sb.append(f.name().toLowerCase());
//    }
//    throw new UnsupportedFeatureException(
//        "Invalid value '" + s + "' for a date/time field. Valid values are: " + sb.toString());
//  }

}
