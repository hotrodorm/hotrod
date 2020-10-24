package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class NumberWindowFunctionFrameUnitStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionFrameUnitStage(final NumberWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

  public NumberWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

  public NumberWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

  public NumberWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new NumberWindowFunctionFrameStartedStage(this.function);
  }

  public NumberWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new NumberWindowFunctionFrameStartedStage(this.function);
  }

  public NumberWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new NumberWindowFunctionFrameStartedStage(this.function);
  }

  public NumberWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new NumberWindowFunctionFrameStartedStage(this.function);
  }

}
