package org.hotrod.runtime.livesql.expressions.analytics;

public class StringWindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public StringWindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
