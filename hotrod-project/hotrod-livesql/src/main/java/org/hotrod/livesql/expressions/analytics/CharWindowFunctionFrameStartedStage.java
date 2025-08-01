package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameBound;

public class CharWindowFunctionFrameStartedStage {

  private CharWindowExpression function;

  public CharWindowFunctionFrameStartedStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

  public CharWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

  public CharWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

  public CharWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new CharWindowFunctionFrameBoundStage(this.function);
  }

}
