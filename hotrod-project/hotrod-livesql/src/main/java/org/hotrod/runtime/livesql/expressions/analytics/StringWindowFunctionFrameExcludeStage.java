package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.strings.GeneralStringExpression;

public class StringWindowFunctionFrameExcludeStage {

  private StringWindowExpression function;

  public StringWindowFunctionFrameExcludeStage(final StringWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public GeneralStringExpression end() {
    return this.function;
  }

}
