package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.LocalTime;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class LocalTimeLiteral extends TimeLiteral {

  // Constructor

  public LocalTimeLiteral(final LiveSQLContext context, final LocalTime value, final int precision) {
    super();
    String isoTime = value.format(this.getFormatter(precision));
    this.formatted = context.getLiveSQLDialect().getDateTimeLiteralRenderer().renderTime(isoTime, precision);
  }

}
