package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class DeleteFromPhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private DeleteObject delete;

  // Constructor

  public DeleteFromPhase(final LiveSQLContext context, final TableOrView from) {
    this.context = context;
    this.delete = new DeleteObject();
    this.delete.setFrom(from);
  }

  // Next stages

  public DeleteWherePhase where(final Predicate predicate) {
    return new DeleteWherePhase(this.context, this.delete, predicate);
  }

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
