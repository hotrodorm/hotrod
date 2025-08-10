package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;

public class BooleanWindowFunctionOrderingStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionOrderingStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public BooleanWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public BooleanSyntaxExpression end() {
    return this.function;
  }

}
