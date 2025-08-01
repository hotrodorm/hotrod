package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameBound;

public class BinaryWindowFunctionFrameStartedStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionFrameStartedStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BinaryWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

  public BinaryWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

  public BinaryWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

  public BinaryWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new BinaryWindowFunctionFrameBoundStage(this.function);
  }

}
