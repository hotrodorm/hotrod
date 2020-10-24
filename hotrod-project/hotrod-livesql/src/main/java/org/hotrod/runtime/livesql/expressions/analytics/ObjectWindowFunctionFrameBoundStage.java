package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;

public class ObjectWindowFunctionFrameBoundStage {

  private WindowExpression function;

  public ObjectWindowFunctionFrameBoundStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages ObjectWindowFunction

  public ObjectWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
