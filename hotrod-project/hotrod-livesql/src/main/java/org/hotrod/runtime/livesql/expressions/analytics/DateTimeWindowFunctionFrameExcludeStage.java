package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.datetime.GeneralDateTimeExpression;

public class DateTimeWindowFunctionFrameExcludeStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionFrameExcludeStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public GeneralDateTimeExpression end() {
    return this.function;
  }

}
