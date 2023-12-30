package org.hotrod.runtime.livesql.queries;

import java.util.Map;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class UpdateSetCompletePhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateSetCompletePhase(final LiveSQLContext context, final UpdateObject update) {
    this.context = context;
    this.update = update;
  }

  public UpdateSetCompletePhase(final LiveSQLContext context, final String mapperStatement,
      final TableOrView tableOrView, final Predicate predicate, final Map<String, Object> extraSets) {
    this.context = context;
    this.update = new UpdateObject(mapperStatement);
    this.update.setTableOrView(tableOrView);
    this.update.setWherePredicate(predicate);
    this.update.setExtraSets(extraSets);
  }

  // Current phase

  // Next phases

  public UpdateWherePhase where(final Predicate predicate) {
    return new UpdateWherePhase(this.context, this.update, predicate);
  }

  // Preview

  @Override
  public String getPreview() {
    return this.update.getPreview(this.context);
  }

  // Execute

  @Override
  public int execute() {
    return this.update.execute(this.context);
  }

}
