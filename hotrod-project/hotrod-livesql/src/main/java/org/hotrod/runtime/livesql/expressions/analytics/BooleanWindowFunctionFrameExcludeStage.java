package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanWindowFunctionFrameExcludeStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionFrameExcludeStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public Predicate end() {
    return this.function;
  }

}
