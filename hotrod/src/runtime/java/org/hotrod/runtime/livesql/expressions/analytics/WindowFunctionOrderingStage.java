package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class WindowFunctionOrderingStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionOrderingStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameUnitStage<T> rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new WindowFunctionFrameUnitStage<T>(this.function);
  }

  public WindowFunctionFrameUnitStage<T> range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new WindowFunctionFrameUnitStage<T>(this.function);
  }

  public WindowFunctionFrameUnitStage<T> groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new WindowFunctionFrameUnitStage<T>(this.function);
  }

  public WindowExpression<T> end() {
    return this.function;
  }

}
