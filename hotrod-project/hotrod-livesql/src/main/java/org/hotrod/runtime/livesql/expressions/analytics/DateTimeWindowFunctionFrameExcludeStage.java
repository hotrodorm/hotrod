package org.hotrod.runtime.livesql.expressions.analytics;

public class DateTimeWindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public DateTimeWindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
