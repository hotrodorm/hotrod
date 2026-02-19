package org.hotrod.livesql.queries;

import org.hotrod.livesql.LiveSQLLogging;

public class GeneratedKeysInsertValuesPhase<T> implements GeneratedKeysQuery<T> {

  // Properties

  private LiveSQLContext context;
  private GeneratedKeysInsertObject<T> insert;

  // Constructor

  public GeneratedKeysInsertValuesPhase(final LiveSQLContext context, final GeneratedKeysInsertObject<T> insert) {
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
  public T execute() {
    return this.insert.executeOne(this.context);
  }

  @Override
  public T execute(LiveSQLLogging loggingAdapter) {
    return this.insert.executeOne(this.context, loggingAdapter);
  }

}
