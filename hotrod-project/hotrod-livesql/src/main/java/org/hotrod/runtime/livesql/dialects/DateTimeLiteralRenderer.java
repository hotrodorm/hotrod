package org.hotrod.runtime.livesql.dialects;

public abstract class DateTimeLiteralRenderer {

  public abstract String renderDate(final String isoDate);

  public abstract String renderTime(final String isoTime, final int precision);

  public abstract String renderTimestamp(final String isoTimestamp, final int precision);

  public abstract String renderOffsetTime(final String isoTime, final String isoOffset, final int precision);

  public abstract String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision);

}
