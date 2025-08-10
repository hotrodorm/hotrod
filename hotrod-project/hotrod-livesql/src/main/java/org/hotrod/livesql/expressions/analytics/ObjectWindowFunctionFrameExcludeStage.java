package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.object.ObjectSyntaxExpression;

public class ObjectWindowFunctionFrameExcludeStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionFrameExcludeStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectSyntaxExpression end() {
    return this.function;
  }

}
