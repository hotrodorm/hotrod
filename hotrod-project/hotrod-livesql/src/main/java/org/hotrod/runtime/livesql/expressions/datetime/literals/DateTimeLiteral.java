package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

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

  // ========================================0

  private void test() {

    int year = 2023;
    int month = 10;
    int day = 26;
    int hour = 14;
    int minute = 4;
    int second = 36;
    int nanosecond = 73890346;
    int offsetHours = -4;
    int offsetMinutes = 30;
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(offsetHours, offsetMinutes);
    ZoneId zone = ZoneId.of("America/NewYork");

//    DateTimeLiteral;
//      - LocalDateLiteral
//      - TimeLiteral:
//          - LocalTimeLiteral
//          - OffsetTimeLiteral
//      - TimestampLiteral:
//          - LocalTimestampLiteral
//          - OffsetTimestampLiteral

    // SQL DATE (local date)

    LocalDate a = LocalDate.of(2015, 02, 20);

    // SQL TIMESTAMP (local timestamp)

    LocalDateTime b = LocalDateTime.of(year, month, day, hour, minute, second);
    LocalDateTime c = LocalDateTime.of(year, month, day, hour, minute, second, nanosecond);

    Date a1 = new Date();

    new Timestamp(a1.getTime()).toLocalDateTime();

    Timestamp ts = new Timestamp(System.currentTimeMillis());
    ts.toLocalDateTime();

    // SQL TIMESTAMP WITH TIME ZONE

    OffsetDateTime t0 = OffsetDateTime.of(year, month, day, hour, minute, second, nanosecond, offset);

    ZonedDateTime t1 = ZonedDateTime.of(year, month, day, offsetHours, offsetMinutes, second, nanosecond, zone);

    // SQL TIME (local time)

    LocalTime d = LocalTime.of(hour, minute, second);
    LocalTime e = LocalTime.of(hour, minute, second, nanosecond);

    java.sql.Time t5 = new java.sql.Time(System.currentTimeMillis());
    t5.toLocalTime();

    // SQL TIME WITH TIME ZONE

    OffsetTime t3 = OffsetTime.of(offsetHours, offsetMinutes, second, nanosecond, offset);
  }

}
