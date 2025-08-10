package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharSyntaxExpression;

public class CharWindowFunctionFrameExcludeStage {

  private CharWindowExpression function;

  public CharWindowFunctionFrameExcludeStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharSyntaxExpression end() {
    return this.function;
  }

}
