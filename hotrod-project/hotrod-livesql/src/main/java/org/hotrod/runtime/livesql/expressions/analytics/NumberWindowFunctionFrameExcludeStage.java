package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;

public class NumberWindowFunctionFrameExcludeStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionFrameExcludeStage(final NumberWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public GeneralNumberExpression end() {
    return this.function;
  }

}
