package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.livesql.expressions.object.ObjectSyntaxExpression;

public class ObjectWindowFunctionFrameBoundStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionFrameBoundStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages ObjectWindowFunction

  public ObjectWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new ObjectWindowFunctionFrameExcludeStage(this.function);
  }

  public ObjectSyntaxExpression end() {
    return this.function;
  }

}
