package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class ObjectWindowFunctionFrameStartedStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionFrameStartedStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

  public ObjectWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

  public ObjectWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

  public ObjectWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

}
