package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class DeleteWherePhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private DeleteObject delete;

  // Constructor

  public DeleteWherePhase(final LiveSQLContext context, final DeleteObject delete, final Predicate predicate) {
    this.context = context;
    this.delete = delete;
    this.delete.setWherePredicate(predicate);
  }

  public DeleteWherePhase(final LiveSQLContext context, final String mapperStatement, final TableOrView from,
      final Predicate predicate) {
    this.context = context;
    this.delete = new DeleteObject(mapperStatement);
    this.delete.setFrom(from);
    this.delete.setWherePredicate(predicate);
  }

  // Next stages

  // Preview

  @Override
  public String getPreview() {
    return this.delete.getPreview(this.context);
  }

  // Execute

  @Override
  public int execute() {
    return this.delete.execute(this.context);
  }

}
