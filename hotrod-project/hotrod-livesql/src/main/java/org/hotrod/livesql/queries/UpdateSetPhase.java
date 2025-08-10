package org.hotrod.livesql.queries;

import java.util.Date;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.metadata.BooleanEntityColumn;
import org.hotrod.livesql.metadata.BinaryEntityColumn;
import org.hotrod.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.livesql.metadata.NumericEntityColumn;
import org.hotrod.livesql.metadata.ObjectEntityColumn;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.util.BoxUtil;

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

  public UpdateSetPhase set(final NumericEntityColumn column, final NumericExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumericEntityColumn column, final Number n) {
    this.update.addSetter(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumn column, final CharExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumn column, final String s) {
    this.update.addSetter(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final DateTimeExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final Date dt) {
    this.update.addSetter(column, BoxUtil.box(dt));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final BooleanExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final boolean b) {
    this.update.addSetter(column, BoxUtil.box(b));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumn column, final BinaryExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumn column, final byte[] a) {
    this.update.addSetter(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final ObjectExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final Object o) {
    this.update.addSetter(column, BoxUtil.box(o));
    return new UpdateSetPhase(this.context, this.update);
  }

  // Next phases

  public UpdateWherePhase where(final BooleanExpression predicate) {
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

}
