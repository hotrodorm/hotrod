package org.hotrod.runtime.livesql.queries;

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
    return this.insert.getPreview(this.context);
  }

  // Execute

  @Override
  public int execute() {
    return this.insert.execute(this.context);
  }

}