package org.hotrod.livesql.queries;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class UpdateSetCompletePhase implements DMLQuery {

  private static final Logger log = Logger.getLogger(UpdateSetCompletePhase.class.getName());

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  public static class Setter {

    private EntityColumnMetadata column;
    private Expression expression;

    public Setter(EntityColumnMetadata column, Expression expression) {
      this.column = column;
      this.expression = expression;
    }

    public EntityColumnMetadata getColumn() {
      return column;
    }

    public Expression getExpression() {
      return expression;
    }

  }

  // Constructor

  public UpdateSetCompletePhase(final LiveSQLContext context, final UpdateObject update) {
    log.fine("init");
    this.context = context;
    this.update = update;
  }

  public UpdateSetCompletePhase(final LiveSQLContext context, final TableOrView<?> tableOrView,
      final List<Setter> setters, final Predicate predicate) {
    this.context = context;
    this.update = new UpdateObject();
    this.update.setTableOrView(tableOrView);
    for (Setter s : setters) {
      this.update.addSetter(s.getColumn(), s.getExpression());
    }
    this.update.setWherePredicate(predicate);
  }

  // Current phase

  // Next phases

  public UpdateWherePhase where(final Predicate predicate) {
    return new UpdateWherePhase(this.context, this.update, predicate);
  }

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

  @Override
  public int execute(LiveSQLLogging loggingAdapter) {
    return this.update.execute(this.context, loggingAdapter);
  }

}
