package org.hotrod.runtime.livesql.expressions.datetime.literals;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class OffsetTimeLiteral extends TimeLiteral {

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // Constructor

  public OffsetTimeLiteral(final OffsetTime value) {
    super();
    this.formatted = value.format(FMT);
  }

}
