package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;

public class ByteArrayWindowFunctionOrderingStage {

  private WindowExpression function;

  public ByteArrayWindowFunctionOrderingStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new ByteArrayWindowFunctionFrameUnitStage(this.function);
  }

  public ByteArrayWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new ByteArrayWindowFunctionFrameUnitStage(this.function);
  }

  public ByteArrayWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new ByteArrayWindowFunctionFrameUnitStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
