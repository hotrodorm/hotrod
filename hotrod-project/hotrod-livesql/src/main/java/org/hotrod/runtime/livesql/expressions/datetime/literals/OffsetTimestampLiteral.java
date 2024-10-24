package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.OffsetDateTime;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class OffsetTimestampLiteral extends TimestampLiteral {

  // Constructor

  public OffsetTimestampLiteral(final LiveSQLContext context, final OffsetDateTime value, final int precision) {
    super();
    if (value == null) {
      throw new InvalidLiteralException("A null value is not accepted for an LiveSQL OffsetTimestamp literal.");
    }
    String isoTimestamp = value.format(this.getFormatter(precision));
    String isoOffset = value.getOffset().getId();
    isoOffset = "Z".equals(isoOffset) ? "+00:00" : isoOffset;
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderOffsetTimestamp(isoTimestamp,
        isoOffset, precision);
  }

}
