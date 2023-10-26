package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class LocalDateLiteral extends DateTimeLiteral {

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // Constructor

  public LocalDateLiteral(final LiveSQLContext context, final LocalDate value) {
    super();
    String iso = value.format(FMT);
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderDate(iso);
  }

  public LocalDateLiteral(final LiveSQLContext context, final Date value) {
    super();
    LocalDate localDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String iso = localDate.format(FMT);
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderDate(iso);
  }

}
