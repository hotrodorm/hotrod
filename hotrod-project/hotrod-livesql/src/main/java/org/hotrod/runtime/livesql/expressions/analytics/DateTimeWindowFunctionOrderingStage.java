package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class DateTimeWindowFunctionOrderingStage {

  private WindowExpression function;

  public DateTimeWindowFunctionOrderingStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new DateTimeWindowFunctionFrameUnitStage(this.function);
  }

  public DateTimeWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new DateTimeWindowFunctionFrameUnitStage(this.function);
  }

  public DateTimeWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new DateTimeWindowFunctionFrameUnitStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
