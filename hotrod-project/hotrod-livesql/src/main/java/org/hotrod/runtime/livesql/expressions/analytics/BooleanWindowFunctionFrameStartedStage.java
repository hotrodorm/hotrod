package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class BooleanWindowFunctionFrameStartedStage {

  private WindowExpression function;

  public BooleanWindowFunctionFrameStartedStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

  public BooleanWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

  public BooleanWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

  public BooleanWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

}
