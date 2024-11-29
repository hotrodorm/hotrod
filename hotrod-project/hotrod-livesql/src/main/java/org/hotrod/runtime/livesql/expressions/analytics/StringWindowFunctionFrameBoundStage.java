package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;

public class StringWindowFunctionFrameBoundStage {

  private StringWindowExpression function;

  public StringWindowFunctionFrameBoundStage(final StringWindowExpression function) {
    this.function = function;
  }

  // Next stages StringWindowFunction

  public StringWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new StringWindowFunctionFrameExcludeStage(this.function);
  }

  public StringWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new StringWindowFunctionFrameExcludeStage(this.function);
  }

  public StringWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new StringWindowFunctionFrameExcludeStage(this.function);
  }

  public StringWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new StringWindowFunctionFrameExcludeStage(this.function);
  }

  public StringExpression end() {
    return this.function;
  }

}
