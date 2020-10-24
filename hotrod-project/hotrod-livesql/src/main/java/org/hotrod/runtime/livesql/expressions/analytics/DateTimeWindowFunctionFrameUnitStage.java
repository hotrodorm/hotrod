package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class DateTimeWindowFunctionFrameUnitStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionFrameUnitStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

  public DateTimeWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

  public DateTimeWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

  public DateTimeWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new DateTimeWindowFunctionFrameStartedStage(this.function);
  }

  public DateTimeWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new DateTimeWindowFunctionFrameStartedStage(this.function);
  }

  public DateTimeWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new DateTimeWindowFunctionFrameStartedStage(this.function);
  }

  public DateTimeWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new DateTimeWindowFunctionFrameStartedStage(this.function);
  }

}
