package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.livesql.expressions.binary.BinarySyntaxExpression;

public class BinaryWindowFunctionOrderingStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionOrderingStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BinaryWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new BinaryWindowFunctionFrameUnitStage(this.function);
  }

  public BinaryWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new BinaryWindowFunctionFrameUnitStage(this.function);
  }

  public BinarySyntaxExpression end() {
    return this.function;
  }

}
