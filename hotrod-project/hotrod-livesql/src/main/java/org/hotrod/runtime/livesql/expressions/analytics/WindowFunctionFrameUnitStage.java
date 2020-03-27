package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class WindowFunctionFrameUnitStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionFrameUnitStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameBoundStage<T> unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

  public WindowFunctionFrameBoundStage<T> preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

  public WindowFunctionFrameBoundStage<T> currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new WindowFunctionFrameBoundStage<T>(this.function);
  }

  public WindowFunctionFrameStartedStage<T> betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new WindowFunctionFrameStartedStage<T>(this.function);
  }

  public WindowFunctionFrameStartedStage<T> betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new WindowFunctionFrameStartedStage<T>(this.function);
  }

  public WindowFunctionFrameStartedStage<T> betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new WindowFunctionFrameStartedStage<T>(this.function);
  }

  public WindowFunctionFrameStartedStage<T> betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new WindowFunctionFrameStartedStage<T>(this.function);
  }

}
