package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinarySyntaxExpression;

public class BinaryWindowFunctionFrameExcludeStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionFrameExcludeStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BinarySyntaxExpression end() {
    return this.function;
  }

}
