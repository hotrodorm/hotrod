package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;

public class DateTimeWindowFunctionFrameStartedStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionFrameStartedStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionFrameBoundStage andPreceding(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_PRECEDING, offset);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

  public DateTimeWindowFunctionFrameBoundStage andCurrentRow() {
    this.function.setFrameEnd(FrameBound.CURRENT_ROW, null);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

  public DateTimeWindowFunctionFrameBoundStage andFollowing(final int offset) {
    this.function.setFrameEnd(FrameBound.OFFSET_FOLLOWING, offset);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

  public DateTimeWindowFunctionFrameBoundStage andUnboundedFollowing() {
    this.function.setFrameEnd(FrameBound.UNBOUNDED_FOLLOWING, null);
    return new DateTimeWindowFunctionFrameBoundStage(this.function);
  }

}
