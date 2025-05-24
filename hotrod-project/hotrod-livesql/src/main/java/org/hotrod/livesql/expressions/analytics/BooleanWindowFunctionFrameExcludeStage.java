package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.predicates.BooleanExpression;

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
