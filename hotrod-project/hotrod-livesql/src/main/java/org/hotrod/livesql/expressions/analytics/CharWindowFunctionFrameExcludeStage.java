package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharExpression;

public class CharWindowFunctionFrameExcludeStage {

  private CharWindowExpression function;

  public CharWindowFunctionFrameExcludeStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharExpression end() {
    return this.function;
  }

}
