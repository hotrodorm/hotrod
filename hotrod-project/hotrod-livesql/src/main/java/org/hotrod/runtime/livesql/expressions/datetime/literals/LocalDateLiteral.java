package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class LocalDateLiteral extends DateTimeLiteral {

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // Constructor

  public LocalDateLiteral(final LiveSQLContext context, final LocalDate value) {
    super();
    String iso = value.format(FMT);
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderDate(iso);
  }

}
