package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;

public class NumericWindowFunctionOrderingStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionOrderingStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new NumericWindowFunctionFrameUnitStage(this.function);
  }

  public NumericWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new NumericWindowFunctionFrameUnitStage(this.function);
  }

  public NumericWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new NumericWindowFunctionFrameUnitStage(this.function);
  }

  public NumericSyntaxExpression end() {
    return this.function;
  }

}
