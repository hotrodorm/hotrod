package org.hotrod.runtime.livesql.expressions.analytics;

public class WindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public WindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
