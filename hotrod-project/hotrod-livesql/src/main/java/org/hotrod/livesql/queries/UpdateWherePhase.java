package org.hotrod.livesql.queries;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class UpdateWherePhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateWherePhase(final LiveSQLContext context, final UpdateObject update,
      final Predicate predicate) {
    this.context = context;
    this.update = update;
    this.update.setWherePredicate(predicate);
  }

  // Next phases

  // Preview

  @Override
  public String getPreview() {
    return this.update.getPreview(this.context, false);
  }

  @Override
  public String getPreview(boolean includeParameters) {
    return this.update.getPreview(this.context, includeParameters);
  }

  // Execute

  @Override
  public int execute() {
    return this.update.execute(this.context);
  }

}
