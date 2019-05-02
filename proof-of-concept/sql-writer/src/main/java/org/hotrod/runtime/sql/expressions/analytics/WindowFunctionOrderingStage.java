package org.hotrod.runtime.sql.expressions.analytics;

public class WindowFunctionOrderingStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionOrderingStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression<T> end() {
    return this.function;
  }

}
