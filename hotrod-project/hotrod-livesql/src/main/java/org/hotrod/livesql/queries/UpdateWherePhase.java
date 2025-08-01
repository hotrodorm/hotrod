package org.hotrod.livesql.queries;

import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;

public class UpdateWherePhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateWherePhase(final LiveSQLContext context, final UpdateObject update,
      final GeneralBooleanExpression predicate) {
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
