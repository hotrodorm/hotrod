package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.strings.StringExpression;

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
