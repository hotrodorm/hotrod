package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class NumberWindowFunctionOrderingStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionOrderingStage(final NumberWindowExpression function) {
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

  public NumberExpression end() {
    return this.function;
  }

}
