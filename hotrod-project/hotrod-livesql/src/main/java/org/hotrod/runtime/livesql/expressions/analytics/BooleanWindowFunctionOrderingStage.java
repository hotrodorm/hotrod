package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class BooleanWindowFunctionOrderingStage {

  private WindowExpression function;

  public BooleanWindowFunctionOrderingStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public BooleanWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public BooleanWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
