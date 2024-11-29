package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;

public class DateTimeWindowFunctionFrameExcludeStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionFrameExcludeStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeExpression end() {
    return this.function;
  }

}
