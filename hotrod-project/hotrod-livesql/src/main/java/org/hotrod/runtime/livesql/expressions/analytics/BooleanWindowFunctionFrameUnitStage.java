package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class BooleanWindowFunctionFrameUnitStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionFrameUnitStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

  public BooleanWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

  public BooleanWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new BooleanWindowFunctionFrameBoundStage(this.function);
  }

  public BooleanWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new BooleanWindowFunctionFrameStartedStage(this.function);
  }

  public BooleanWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new BooleanWindowFunctionFrameStartedStage(this.function);
  }

  public BooleanWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new BooleanWindowFunctionFrameStartedStage(this.function);
  }

  public BooleanWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new BooleanWindowFunctionFrameStartedStage(this.function);
  }

}
