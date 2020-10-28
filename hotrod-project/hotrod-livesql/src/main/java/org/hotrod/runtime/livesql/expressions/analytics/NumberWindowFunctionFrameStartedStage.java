package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class NumberWindowFunctionFrameStartedStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionFrameStartedStage(final NumberWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

  public NumberWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

  public NumberWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

  public NumberWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new NumberWindowFunctionFrameBoundStage(this.function);
  }

}
