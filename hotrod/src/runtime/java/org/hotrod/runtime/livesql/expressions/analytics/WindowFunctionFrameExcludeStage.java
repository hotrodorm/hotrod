package org.hotrod.runtime.livesql.expressions.analytics;

public class WindowFunctionFrameExcludeStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionFrameExcludeStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression<T> end() {
    return this.function;
  }

}
