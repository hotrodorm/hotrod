package org.hotrod.livesql.queries;

import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.TableOrView;

public class DeleteWherePhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private DeleteObject delete;

  // Constructor

  public DeleteWherePhase(final LiveSQLContext context, final DeleteObject delete,
      final GeneralBooleanExpression predicate) {
    this.context = context;
    this.delete = delete;
    this.delete.setWherePredicate(predicate);
  }

  public DeleteWherePhase(final LiveSQLContext context, final TableOrView from,
      final GeneralBooleanExpression predicate) {
    this.context = context;
    this.delete = new DeleteObject();
    this.delete.setFrom(from);
    this.delete.setWherePredicate(predicate);
  }

  // Next stages

  // Preview

  @Override
  public String getPreview() {
    return this.delete.getPreview(this.context, false);
  }

  @Override
  public String getPreview(boolean includeParameters) {
    return this.delete.getPreview(this.context, includeParameters);
  }

  // Execute

  @Override
  public int execute() {
    return this.delete.execute(this.context);
  }

}
