package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameBound;

public class NumericWindowFunctionFrameUnitStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionFrameUnitStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

  public NumericWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

  public NumericWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

  public NumericWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new NumericWindowFunctionFrameStartedStage(this.function);
  }

  public NumericWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new NumericWindowFunctionFrameStartedStage(this.function);
  }

  public NumericWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new NumericWindowFunctionFrameStartedStage(this.function);
  }

  public NumericWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new NumericWindowFunctionFrameStartedStage(this.function);
  }

}
