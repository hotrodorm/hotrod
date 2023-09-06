package org.hotrod.runtime.livesql.queries;

public class InsertSelectPhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private InsertObject insert;

  // Constructor

  public InsertSelectPhase(final LiveSQLContext context, final InsertObject insert) {
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

  public void execute() {
    this.insert.execute(this.context);
  }

}
