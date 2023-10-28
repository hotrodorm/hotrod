package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class LocalDateLiteral extends DateTimeLiteral {

  private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // Constructor

  public LocalDateLiteral(final LiveSQLContext context, final LocalDate value) {
    super();
    String isoDate = value.format(ISO_DATE_FORMATTER);
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderDate(isoDate);
  }

}
