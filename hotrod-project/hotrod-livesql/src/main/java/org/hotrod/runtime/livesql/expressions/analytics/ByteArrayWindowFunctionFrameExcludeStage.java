package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.binary.GeneralByteArrayExpression;

public class ByteArrayWindowFunctionFrameExcludeStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionFrameExcludeStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public GeneralByteArrayExpression end() {
    return this.function;
  }

}
