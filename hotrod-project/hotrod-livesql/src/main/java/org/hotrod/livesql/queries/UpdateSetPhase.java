package org.hotrod.livesql.queries;

import java.util.Date;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.metadata.BinaryEntityColumnMetaData;
import org.hotrod.livesql.metadata.BooleanEntityColumnMetaData;
import org.hotrod.livesql.metadata.CharEntityColumnMetaData;
import org.hotrod.livesql.metadata.DateTimeEntityColumnMetaData;
import org.hotrod.livesql.metadata.NumericEntityColumnMetaData;
import org.hotrod.livesql.metadata.ObjectEntityColumnMetaData;
import org.hotrod.livesql.util.BoxUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class UpdateSetPhase implements DMLQuery {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateSetPhase(final LiveSQLContext context, final UpdateObject update) {
    this.context = context;
    this.update = update;
  }

  // Current phase

  public UpdateSetPhase set(final NumericEntityColumnMetaData column, final NumericExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumericEntityColumnMetaData column, final Number n) {
    this.update.addSetter(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumnMetaData column, final CharExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumnMetaData column, final String s) {
    this.update.addSetter(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumnMetaData column, final DateTimeExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumnMetaData column, final Date dt) {
    this.update.addSetter(column, BoxUtil.box(dt));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumnMetaData column, final Predicate expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumnMetaData column, final boolean b) {
    this.update.addSetter(column, BoxUtil.box(b));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumnMetaData column, final BinaryExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumnMetaData column, final byte[] a) {
    this.update.addSetter(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumnMetaData column, final ObjectExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumnMetaData column, final Object o) {
    this.update.addSetter(column, BoxUtil.box(o));
    return new UpdateSetPhase(this.context, this.update);
  }

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
