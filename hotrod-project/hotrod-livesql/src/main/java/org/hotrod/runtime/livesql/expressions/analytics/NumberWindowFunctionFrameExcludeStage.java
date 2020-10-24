package org.hotrod.runtime.livesql.expressions.analytics;

public class NumberWindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public NumberWindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
