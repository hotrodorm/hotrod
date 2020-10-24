package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;

public class ByteArrayWindowFunctionFrameBoundStage {

  private WindowExpression function;

  public ByteArrayWindowFunctionFrameBoundStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages ByteArrayWindowFunction

  public ByteArrayWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new ByteArrayWindowFunctionFrameExcludeStage(this.function);
  }

  public ByteArrayWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new ByteArrayWindowFunctionFrameExcludeStage(this.function);
  }

  public ByteArrayWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new ByteArrayWindowFunctionFrameExcludeStage(this.function);
  }

  public ByteArrayWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new ByteArrayWindowFunctionFrameExcludeStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
