package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class WindowFunctionFrameStartedStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionFrameStartedStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameBoundStage<T> andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

  public WindowFunctionFrameBoundStage<T> andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

  public WindowFunctionFrameBoundStage<T> andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

  public WindowFunctionFrameBoundStage<T> andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

}
