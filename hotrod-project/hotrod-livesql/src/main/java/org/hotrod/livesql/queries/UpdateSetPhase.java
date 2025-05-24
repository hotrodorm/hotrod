package org.hotrod.livesql.queries;

import java.util.Date;

import org.hotrod.livesql.expressions.binary.GeneralByteArrayExpression;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.metadata.BooleanEntityColumn;
import org.hotrod.livesql.metadata.ByteArrayEntityColumn;
import org.hotrod.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.livesql.metadata.NumberEntityColumn;
import org.hotrod.livesql.metadata.ObjectEntityColumn;
import org.hotrod.livesql.metadata.StringEntityColumn;
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

  public UpdateSetPhase set(final NumberEntityColumn column, final GeneralNumberExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumberEntityColumn column, final Number n) {
    this.update.addSetter(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final GeneralStringExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final String s) {
    this.update.addSetter(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final GeneralDateTimeExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final Date dt) {
    this.update.addSetter(column, BoxUtil.box(dt));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final GeneralBooleanExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final boolean b) {
    this.update.addSetter(column, BoxUtil.box(b));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final GeneralByteArrayExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final byte[] a) {
    this.update.addSetter(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final GeneralObjectExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final Object o) {
    this.update.addSetter(column, BoxUtil.box(o));
    return new UpdateSetPhase(this.context, this.update);
  }

  // Next phases

  public UpdateWherePhase where(final GeneralBooleanExpression predicate) {
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
