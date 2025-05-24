package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.ByteArrayExpression;

public class ByteArrayWindowFunctionFrameExcludeStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionFrameExcludeStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayExpression end() {
    return this.function;
  }

}
