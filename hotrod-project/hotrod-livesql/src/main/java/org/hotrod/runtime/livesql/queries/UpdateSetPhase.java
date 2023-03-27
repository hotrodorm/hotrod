package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;

public class UpdateSetPhase implements ExecutableQuery {

  // Properties

  private Update update;

  // Constructor

  public UpdateSetPhase(final Update update) {
    this.update = update;
  }

  // Current phase

  public UpdateSetPhase set(final Column column, final Expression expression) {
    this.update.addSet(column, expression);
    return this;
  }

  // Next phases

  public UpdateWherePhase where(final Predicate predicate) {
    return new UpdateWherePhase(this.update, predicate);
  }

  // Preview

  @Override
  public String getPreview() {
    return this.update.getPreview();
  }

  // Execute

  @Override
  public void execute() {
    this.update.execute();
  }

}
