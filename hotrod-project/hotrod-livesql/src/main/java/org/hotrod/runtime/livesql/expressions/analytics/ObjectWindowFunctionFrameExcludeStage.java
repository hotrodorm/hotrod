package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

public class ObjectWindowFunctionFrameExcludeStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionFrameExcludeStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectExpression end() {
    return this.function;
  }

}
