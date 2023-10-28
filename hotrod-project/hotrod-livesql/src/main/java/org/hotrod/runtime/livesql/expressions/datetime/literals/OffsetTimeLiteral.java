package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.OffsetTime;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class OffsetTimeLiteral extends TimeLiteral {

  // Constructor

  public OffsetTimeLiteral(final LiveSQLContext context, final OffsetTime value, final int precision) {
    super();
    if (value == null) {
      throw new InvalidLiteralException("A null value is not accepted for a LiveSQL OffsetTime literal.");
    }
    String isoTime = value.format(this.getFormatter(precision));
    String isoOffset = value.getOffset().getId();
    isoOffset = "Z".equals(isoOffset) ? "+00:00" : isoOffset;
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderOffsetTime(isoTime, isoOffset,
        precision);
  }

}
