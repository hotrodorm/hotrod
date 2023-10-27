package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.format.DateTimeFormatter;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;

public abstract class TimestampLiteral extends DateTimeLiteral {

  private static final DateTimeFormatter[] ISO_TIMESTAMP_FORMATTERS = { //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSS"), //
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS") //
  };

  protected DateTimeFormatter getFormatter(final int precision) {
    if (precision < 0 || precision > ISO_TIMESTAMP_FORMATTERS.length - 1) {
      throw new InvalidLiteralException("A TIME literal's precision must be between zero and "
          + (ISO_TIMESTAMP_FORMATTERS.length - 1) + ", but it was specified as " + precision + ".");
    }
    return ISO_TIMESTAMP_FORMATTERS[precision];
  }

}
