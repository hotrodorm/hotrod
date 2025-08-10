package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.livesql.expressions.object.ObjectSyntaxExpression;

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

  public ObjectWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new ObjectWindowFunctionFrameUnitStage(this.function);
  }

  public ObjectSyntaxExpression end() {
    return this.function;
  }

}
