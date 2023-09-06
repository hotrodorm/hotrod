package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class DeleteFromPhase implements ExecutableQuery {

  // Properties

  private Delete delete;

  // Constructor

  public DeleteFromPhase(final LiveSQLContext context, final TableOrView from) {
    this.delete = new Delete(context);
    this.delete.setFrom(from);
  }

  // Next stages

  public DeleteWherePhase where(final Predicate predicate) {
    return new DeleteWherePhase(this.delete, predicate);
  }

  // Preview

  @Override
  public String getPreview() {
    return this.delete.getPreview();
  }

  // Execute

  @Override
  public void execute() {
    this.delete.execute();
  }

}
