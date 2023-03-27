package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class DeleteWherePhase implements ExecutableQuery {

  // Properties

  private Delete delete;

  // Constructor

  public DeleteWherePhase(final Delete delete, final Predicate predicate) {
    this.delete = delete;
    this.delete.setWherePredicate(predicate);
  }

  // Next stages

  // Preview

  @Override
  public String getPreview() {
    return this.delete.getPreview();
  }

  // Execute

  public void execute() {
    this.delete.execute();
  }

}
