package org.hotrod.runtime.livesql.queries;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.metadata.BooleanEntityColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayEntityColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.runtime.livesql.metadata.NumberEntityColumn;
import org.hotrod.runtime.livesql.metadata.ObjectEntityColumn;
import org.hotrod.runtime.livesql.metadata.StringEntityColumn;
import org.hotrod.runtime.livesql.util.BoxUtil;

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

  public UpdateSetPhase set(final NumberEntityColumn column, final NumberExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumberEntityColumn column, final Number n) {
    this.update.addSet(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final StringExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final String s) {
    this.update.addSet(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final DateTimeExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final Date dt) {
    this.update.addSet(column, BoxUtil.box(dt));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final Predicate expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final boolean b) {
    this.update.addSet(column, BoxUtil.box(b));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final ByteArrayExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final byte[] a) {
    this.update.addSet(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final ObjectExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final Object o) {
    this.update.addSet(column, BoxUtil.box(o));
    return new UpdateSetPhase(this.context, this.update);
  }

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
