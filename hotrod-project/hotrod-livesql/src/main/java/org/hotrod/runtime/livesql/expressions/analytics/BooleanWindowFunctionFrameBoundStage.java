package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;

public class BooleanWindowFunctionFrameBoundStage {

  private WindowExpression function;

  public BooleanWindowFunctionFrameBoundStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages BooleanWindowFunction

  public BooleanWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public BooleanWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public BooleanWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public BooleanWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
