package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class ObjectWindowFunctionFrameUnitStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionFrameUnitStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionFrameBoundStage unboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

  public ObjectWindowFunctionFrameBoundStage preceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

  public ObjectWindowFunctionFrameBoundStage currentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new ObjectWindowFunctionFrameBoundStage(this.function);
  }

  public ObjectWindowFunctionFrameStartedStage betweenUnboundedPreceding() {
    this.function.setFrameStart(FrameBound.UNBOUNDED_PRECEDING, null);
    return new ObjectWindowFunctionFrameStartedStage(this.function);
  }

  public ObjectWindowFunctionFrameStartedStage betweenPreceding(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_PRECEDING, offset);
    return new ObjectWindowFunctionFrameStartedStage(this.function);
  }

  public ObjectWindowFunctionFrameStartedStage betweenCurrentRow() {
    this.function.setFrameStart(FrameBound.CURRENT_ROW, null);
    return new ObjectWindowFunctionFrameStartedStage(this.function);
  }

  public ObjectWindowFunctionFrameStartedStage betweenFollowing(final int offset) {
    this.function.setFrameStart(FrameBound.OFFSET_FOLLOWING, offset);
    return new ObjectWindowFunctionFrameStartedStage(this.function);
  }

}
