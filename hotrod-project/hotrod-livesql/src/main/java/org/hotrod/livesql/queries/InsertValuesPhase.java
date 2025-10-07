package org.hotrod.livesql.queries;

import org.hotrod.livesql.LiveSQLLogging;

public class InsertValuesPhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private InsertObject insert;

  // Constructor

  public InsertValuesPhase(final LiveSQLContext context, final InsertObject insert) {
    this.context = context;
    this.insert = insert;
  }

  // Next stages

  // Preview

  @Override
  public String getPreview() {
    return this.insert.getPreview(this.context, false);
  }

  @Override
  public String getPreview(boolean includeParameters) {
    return this.insert.getPreview(this.context, includeParameters);
  }

  // Execute

  @Override
  public int execute() {
    return this.insert.execute(this.context);
  }

  @Override
  public int execute(LiveSQLLogging loggingAdapter) {
    return this.insert.execute(this.context, loggingAdapter);
  }

}
