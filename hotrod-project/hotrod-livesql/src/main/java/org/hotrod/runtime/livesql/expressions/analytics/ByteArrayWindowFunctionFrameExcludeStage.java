package org.hotrod.runtime.livesql.expressions.analytics;

public class ByteArrayWindowFunctionFrameExcludeStage {

  private WindowExpression function;

  public ByteArrayWindowFunctionFrameExcludeStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public WindowExpression end() {
    return this.function;
  }

}
