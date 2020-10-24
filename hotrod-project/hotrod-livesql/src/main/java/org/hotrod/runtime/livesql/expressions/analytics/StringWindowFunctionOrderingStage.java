package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class StringWindowFunctionOrderingStage {

  private WindowExpression function;

  public StringWindowFunctionOrderingStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new StringWindowFunctionFrameUnitStage(this.function);
  }

  public StringWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new StringWindowFunctionFrameUnitStage(this.function);
  }

  public StringWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new StringWindowFunctionFrameUnitStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
