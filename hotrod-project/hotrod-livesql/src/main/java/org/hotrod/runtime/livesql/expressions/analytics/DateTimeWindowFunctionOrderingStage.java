package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;

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

  public DateTimeExpression end() {
    return this.function;
  }

}
