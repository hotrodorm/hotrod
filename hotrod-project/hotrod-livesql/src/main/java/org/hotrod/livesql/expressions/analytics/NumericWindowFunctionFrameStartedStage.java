package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameBound;

public class NumericWindowFunctionFrameStartedStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionFrameStartedStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

  public NumericWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

  public NumericWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

  public NumericWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new NumericWindowFunctionFrameBoundStage(this.function);
  }

}
