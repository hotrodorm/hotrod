package org.hotrod.runtime.livesql.queries;

public class InsertValuesPhase implements ExecutableQuery {

  // Properties

  private Insert insert;

  // Constructor

  public InsertValuesPhase(final Insert insert) {
    this.insert = insert;
  }

  // Next stages

  // Preview

  @Override
  public String getPreview() {
    return this.insert.getPreview();
  }

  // Execute

  public void execute() {
    this.insert.execute();
  }

}
