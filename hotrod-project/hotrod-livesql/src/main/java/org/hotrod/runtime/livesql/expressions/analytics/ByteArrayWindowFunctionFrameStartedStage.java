package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class ByteArrayWindowFunctionFrameStartedStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionFrameStartedStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

  public ByteArrayWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

  public ByteArrayWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

  public ByteArrayWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new ByteArrayWindowFunctionFrameBoundStage(this.function);
  }

}
