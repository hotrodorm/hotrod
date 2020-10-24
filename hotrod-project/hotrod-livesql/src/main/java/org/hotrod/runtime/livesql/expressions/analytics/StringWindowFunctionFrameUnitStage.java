package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class StringWindowFunctionFrameUnitStage {

  private StringWindowExpression function;

  public StringWindowFunctionFrameUnitStage(final StringWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

  public StringWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

  public StringWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

  public StringWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new StringWindowFunctionFrameStartedStage(this.function);
  }

  public StringWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new StringWindowFunctionFrameStartedStage(this.function);
  }

  public StringWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new StringWindowFunctionFrameStartedStage(this.function);
  }

  public StringWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new StringWindowFunctionFrameStartedStage(this.function);
  }

}
