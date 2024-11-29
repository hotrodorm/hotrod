package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.object.GeneralObjectExpression;

public class ObjectWindowFunctionFrameExcludeStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionFrameExcludeStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public GeneralObjectExpression end() {
    return this.function;
  }

}
