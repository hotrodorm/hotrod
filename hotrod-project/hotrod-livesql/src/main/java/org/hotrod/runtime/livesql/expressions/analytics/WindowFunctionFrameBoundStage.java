package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;

public class WindowFunctionFrameBoundStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionFrameBoundStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameExcludeStage<T> excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new WindowFunctionFrameExcludeStage<T>(this.function);
  }

  public WindowFunctionFrameExcludeStage<T> excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new WindowFunctionFrameExcludeStage<T>(this.function);
  }

  public WindowFunctionFrameExcludeStage<T> excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new WindowFunctionFrameExcludeStage<T>(this.function);
  }

  public WindowFunctionFrameExcludeStage<T> excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new WindowFunctionFrameExcludeStage<T>(this.function);
  }

  public WindowExpression<T> end() {
    return this.function;
  }

}
