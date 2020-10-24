package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class NumberWindowFunctionOrderingStage {

  private WindowExpression function;

  public NumberWindowFunctionOrderingStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new NumberWindowFunctionFrameUnitStage(this.function);
  }

  public NumberWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new NumberWindowFunctionFrameUnitStage(this.function);
  }

  public NumberWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new NumberWindowFunctionFrameUnitStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
