package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;

public class NumericWindowFunctionFrameExcludeStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionFrameExcludeStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericSyntaxExpression end() {
    return this.function;
  }

}
