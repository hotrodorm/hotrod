package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameBound;

public class CharWindowFunctionFrameUnitStage {

  private CharWindowExpression function;

  public CharWindowFunctionFrameUnitStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

  public CharWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

  public CharWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

  public CharWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new CharWindowFunctionFrameStartedStage(this.function);
  }

  public CharWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new CharWindowFunctionFrameStartedStage(this.function);
  }

  public CharWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new CharWindowFunctionFrameStartedStage(this.function);
  }

  public CharWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new CharWindowFunctionFrameStartedStage(this.function);
  }

}
