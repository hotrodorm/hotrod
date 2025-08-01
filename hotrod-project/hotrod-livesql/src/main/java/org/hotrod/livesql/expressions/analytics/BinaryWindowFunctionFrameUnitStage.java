package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameBound;

public class BinaryWindowFunctionFrameUnitStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionFrameUnitStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BinaryWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

  public BinaryWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

  public BinaryWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

  public BinaryWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new BinaryWindowFunctionFrameStartedStage(this.function);
  }

  public BinaryWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new BinaryWindowFunctionFrameStartedStage(this.function);
  }

  public BinaryWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new BinaryWindowFunctionFrameStartedStage(this.function);
  }

  public BinaryWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new BinaryWindowFunctionFrameStartedStage(this.function);
  }

}
