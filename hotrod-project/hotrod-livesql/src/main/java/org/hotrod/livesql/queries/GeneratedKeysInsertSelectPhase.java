package org.hotrod.livesql.queries;

import java.util.List;

import org.hotrod.livesql.LiveSQLLogging;

public class GeneratedKeysInsertSelectPhase<T> implements GeneratedKeysInsertFromSelectQuery<T> {

  // Properties

  private LiveSQLContext context;
  private GeneratedKeysInsertObject<T> insert;

  // Constructor

  public GeneratedKeysInsertSelectPhase(final LiveSQLContext context, final GeneratedKeysInsertObject<T> insert) {
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

  public List<T> execute() {
    return this.insert.executeList(this.context);
  }

  public List<T> execute(LiveSQLLogging loggingAdapter) {
    return this.insert.executeList(this.context, loggingAdapter);
  }

}
