package org.hotrod.runtime.livesql.dialects;

public abstract class DateTimeLiteralRenderer {

  public abstract String renderDate(final String isoFormat);

  public abstract String renderTime(final String isoFormat, final int precision);

  public abstract String renderTimestamp(final String isoFormat, final int precision);

}
