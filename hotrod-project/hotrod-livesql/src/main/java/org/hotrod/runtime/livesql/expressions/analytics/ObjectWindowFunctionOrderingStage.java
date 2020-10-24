package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

public class ObjectWindowFunctionOrderingStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionOrderingStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new ObjectWindowFunctionFrameUnitStage(this.function);
  }

  public ObjectWindowFunctionFrameUnitStage range() {
    this.function.setFrameUnit(FrameUnit.RANGE);
    return new ObjectWindowFunctionFrameUnitStage(this.function);
  }

  public ObjectWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new ObjectWindowFunctionFrameUnitStage(this.function);
  }

  public ObjectExpression end() {
    return this.function;
  }

}
