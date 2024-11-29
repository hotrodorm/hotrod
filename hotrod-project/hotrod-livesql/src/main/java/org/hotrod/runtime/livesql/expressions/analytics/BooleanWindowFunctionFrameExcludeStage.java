package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;

public class BooleanWindowFunctionFrameExcludeStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionFrameExcludeStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public GeneralBooleanExpression end() {
    return this.function;
  }

}
