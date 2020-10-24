package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;

public class WindowFunctionFrameBoundStage {

  private WindowExpression function;

  public WindowFunctionFrameBoundStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new WindowFunctionFrameExcludeStage(this.function);
  }

  public WindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new WindowFunctionFrameExcludeStage(this.function);
  }

  public WindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new WindowFunctionFrameExcludeStage(this.function);
  }

  public WindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new WindowFunctionFrameExcludeStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
