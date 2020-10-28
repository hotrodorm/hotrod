package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;

public class ByteArrayWindowFunctionOrderingStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionOrderingStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new ByteArrayWindowFunctionFrameUnitStage(this.function);
  }

  public ByteArrayWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new ByteArrayWindowFunctionFrameUnitStage(this.function);
  }

  public ByteArrayExpression end() {
    return this.function;
  }

}
