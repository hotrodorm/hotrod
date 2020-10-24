package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class WindowFunctionOrderingStage {

  private WindowExpression function;

  public WindowFunctionOrderingStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new WindowFunctionFrameUnitStage(this.function);
  }

  public WindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new WindowFunctionFrameUnitStage(this.function);
  }

  public WindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new WindowFunctionFrameUnitStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
