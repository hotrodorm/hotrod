package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.strings.StringExpression;

public class StringWindowFunctionFrameExcludeStage {

  private StringWindowExpression function;

  public StringWindowFunctionFrameExcludeStage(final StringWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringExpression end() {
    return this.function;
  }

}
