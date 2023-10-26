package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class LocalTimeLiteral extends TimeLiteral {

  private static final DateTimeFormatter[] ISO_TIME_FORMATTERS = { //
      DateTimeFormatter.ofPattern("HH:mm:ss"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.S"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSSS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSSS"), //
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSSSS") //
  };

  // Constructor

  public LocalTimeLiteral(final LiveSQLContext context, final LocalTime value, final int precision) {
    super();
    String iso = value.format(this.getFormatter(precision));
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderTime(iso, precision);
  }

  protected DateTimeFormatter getFormatter(final int precision) {
    if (precision < 0 || precision > ISO_TIME_FORMATTERS.length - 1) {
      throw new InvalidLiteralException("A TIME literal's precision must be between zero and "
          + (ISO_TIME_FORMATTERS.length - 1) + ", but it was specified as " + precision + ".");
    }
    return ISO_TIME_FORMATTERS[precision];
  }

}
