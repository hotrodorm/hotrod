package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;

public class BooleanWindowFunctionFrameExcludeStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionFrameExcludeStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanSyntaxExpression end() {
    return this.function;
  }

}
