package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class ByteArrayWindowFunctionFrameUnitStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionFrameUnitStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

  public ByteArrayWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

  public ByteArrayWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

  public ByteArrayWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new ByteArrayWindowFunctionFrameStartedStage(this.function);
  }

  public ByteArrayWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new ByteArrayWindowFunctionFrameStartedStage(this.function);
  }

  public ByteArrayWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new ByteArrayWindowFunctionFrameStartedStage(this.function);
  }

  public ByteArrayWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new ByteArrayWindowFunctionFrameStartedStage(this.function);
  }

}
