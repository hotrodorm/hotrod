package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class WindowFunctionFrameStartedStage {

  private WindowExpression function;

  public WindowFunctionFrameStartedStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new WindowFunctionFrameBoundStage(this.function);
  }

  public WindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new WindowFunctionFrameBoundStage(this.function);
  }

  public WindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new WindowFunctionFrameBoundStage(this.function);
  }

  public WindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new WindowFunctionFrameBoundStage(this.function);
  }

}
