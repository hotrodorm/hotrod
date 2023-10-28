package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.LocalDateTime;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class LocalTimestampLiteral extends TimestampLiteral {

  // Constructor

  public LocalTimestampLiteral(final LiveSQLContext context, final LocalDateTime value, final int precision) {
    super();
    if (value == null) {
      throw new InvalidLiteralException("A null value is not accepted for a LiveSQL LocalTimestamp literal.");
    }
    String isoTimestamp = value.format(this.getFormatter(precision));
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderTimestamp(isoTimestamp, precision);
  }

}
