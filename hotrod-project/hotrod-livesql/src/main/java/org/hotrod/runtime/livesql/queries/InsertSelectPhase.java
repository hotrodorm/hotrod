package org.hotrod.runtime.livesql.queries;

public class InsertSelectPhase implements Query {

  // Properties

  private Insert insert;

  // Constructor

  public InsertSelectPhase(final Insert insert) {
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
