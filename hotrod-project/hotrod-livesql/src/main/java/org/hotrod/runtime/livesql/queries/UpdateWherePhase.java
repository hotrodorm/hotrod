package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class UpdateWherePhase implements Query {

  // Properties

  private Update update;

  // Constructor

  public UpdateWherePhase(final Update update, final Predicate predicate) {
    this.update = update;
    this.update.setWherePredicate(predicate);
  }

  // Next phases

  // Preview

  @Override
  public String getPreview() {
    return this.update.getPreview();
  }

  // Execute

  public void execute() {
    this.update.execute();
  }

}
