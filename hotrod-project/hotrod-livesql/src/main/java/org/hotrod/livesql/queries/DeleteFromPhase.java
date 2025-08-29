package org.hotrod.livesql.queries;

import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

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
