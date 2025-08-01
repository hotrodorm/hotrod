package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.livesql.expressions.character.CharExpression;

public class CharWindowFunctionFrameBoundStage {

  private CharWindowExpression function;

  public CharWindowFunctionFrameBoundStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages StringWindowFunction

  public CharWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new CharWindowFunctionFrameExcludeStage(this.function);
  }

  public CharWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new CharWindowFunctionFrameExcludeStage(this.function);
  }

  public CharWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new CharWindowFunctionFrameExcludeStage(this.function);
  }

  public CharWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new CharWindowFunctionFrameExcludeStage(this.function);
  }

  public CharExpression end() {
    return this.function;
  }

}
