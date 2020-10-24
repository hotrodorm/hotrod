package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;

public class DateTimeWindowFunctionFrameBoundStage {

  private WindowExpression function;

  public DateTimeWindowFunctionFrameBoundStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages DateTimeWindowFunction

  public DateTimeWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new DateTimeWindowFunctionFrameExcludeStage(this.function);
  }

  public DateTimeWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new DateTimeWindowFunctionFrameExcludeStage(this.function);
  }

  public DateTimeWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new DateTimeWindowFunctionFrameExcludeStage(this.function);
  }

  public DateTimeWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new DateTimeWindowFunctionFrameExcludeStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
