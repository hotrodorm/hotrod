package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class StringWindowFunctionFrameStartedStage {

  private WindowExpression function;

  public StringWindowFunctionFrameStartedStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

  public StringWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

  public StringWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

  public StringWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new StringWindowFunctionFrameBoundStage(this.function);
  }

}
