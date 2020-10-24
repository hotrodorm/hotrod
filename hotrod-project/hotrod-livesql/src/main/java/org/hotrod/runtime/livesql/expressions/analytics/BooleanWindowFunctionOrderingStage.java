package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

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

  public BooleanWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public BooleanWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new BooleanWindowFunctionFrameUnitStage(this.function);
  }

  public Predicate end() {
    return this.function;
  }

}
