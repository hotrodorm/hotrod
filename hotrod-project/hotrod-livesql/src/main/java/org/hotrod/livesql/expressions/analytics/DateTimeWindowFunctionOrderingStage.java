package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.livesql.expressions.datetime.DateTimeSyntaxExpression;

public class DateTimeWindowFunctionOrderingStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionOrderingStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new DateTimeWindowFunctionFrameUnitStage(this.function);
  }

  // RANGE is not yet included since the INTERVAL data type is not yet implemented

  public DateTimeWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new DateTimeWindowFunctionFrameUnitStage(this.function);
  }

  public DateTimeSyntaxExpression end() {
    return this.function;
  }

}
