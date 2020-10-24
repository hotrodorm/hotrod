package org.hotrod.runtime.livesql.expressions.analytics;

public class ObjectWindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public ObjectWindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
