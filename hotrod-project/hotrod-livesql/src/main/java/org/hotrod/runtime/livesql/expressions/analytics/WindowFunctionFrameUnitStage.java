package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class WindowFunctionFrameUnitStage {

  private WindowExpression function;

  public WindowFunctionFrameUnitStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new WindowFunctionFrameBoundStage(this.function);
  }

  public WindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new WindowFunctionFrameBoundStage(this.function);
  }

  public WindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new WindowFunctionFrameBoundStage(this.function);
  }

  public WindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new WindowFunctionFrameStartedStage(this.function);
  }

  public WindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new WindowFunctionFrameStartedStage(this.function);
  }

  public WindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new WindowFunctionFrameStartedStage(this.function);
  }

  public WindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new WindowFunctionFrameStartedStage(this.function);
  }

}
