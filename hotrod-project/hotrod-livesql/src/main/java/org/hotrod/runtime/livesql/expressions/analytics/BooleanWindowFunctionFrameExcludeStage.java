package org.hotrod.runtime.livesql.expressions.analytics;

public class BooleanWindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public BooleanWindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
