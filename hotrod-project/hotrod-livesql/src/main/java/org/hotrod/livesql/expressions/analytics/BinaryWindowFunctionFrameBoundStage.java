package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.livesql.expressions.binary.BinaryExpression;

public class BinaryWindowFunctionFrameBoundStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionFrameBoundStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages ByteArrayWindowFunction

  public BinaryWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new BinaryWindowFunctionFrameExcludeStage(this.function);
  }

  public BinaryWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new BinaryWindowFunctionFrameExcludeStage(this.function);
  }

  public BinaryWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new BinaryWindowFunctionFrameExcludeStage(this.function);
  }

  public BinaryWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new BinaryWindowFunctionFrameExcludeStage(this.function);
  }

  public BinaryExpression end() {
    return this.function;
  }

}
