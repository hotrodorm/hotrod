package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanExpression;

public class BooleanWindowFunctionFrameExcludeStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionFrameExcludeStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanExpression end() {
    return this.function;
  }

}
