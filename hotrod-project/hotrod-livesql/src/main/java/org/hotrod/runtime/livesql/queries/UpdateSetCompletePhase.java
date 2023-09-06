package org.hotrod.runtime.livesql.queries;

import java.util.Map;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class UpdateSetCompletePhase implements ExecutableQuery {

  // Properties

  private Update update;

  // Constructor

  public UpdateSetCompletePhase(final Update update) {
    this.update = update;
  }

  public UpdateSetCompletePhase(final LiveSQLContext context, final TableOrView tableOrView,
      final String mapperStatement, final Predicate predicate, final Map<String, Object> extraSets) {
    this.update = new Update(context, mapperStatement);
    this.update.setTableOrView(tableOrView);
    this.update.setWherePredicate(predicate);
    this.update.setExtraSets(extraSets);
  }

  // Current phase

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
