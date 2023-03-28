package org.hotrod.runtime.livesql.queries;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.metadata.BooleanColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeColumn;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.ObjectColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class UpdateSetPhase implements ExecutableQuery {

  // Properties

  private Update update;

  // Constructor

  public UpdateSetPhase(final Update update) {
    this.update = update;
  }

  // Current phase

  public UpdateSetPhase set(final NumberColumn column, final NumberExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final NumberColumn column, final Number n) {
    this.update.addSet(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final StringColumn column, final StringExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final StringColumn column, final String s) {
    this.update.addSet(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final DateTimeColumn column, final DateTimeExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final DateTimeColumn column, final Date dt) {
    this.update.addSet(column, BoxUtil.box(dt));
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final BooleanColumn column, final Predicate expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final BooleanColumn column, final boolean b) {
    this.update.addSet(column, BoxUtil.box(b));
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final ByteArrayColumn column, final ByteArrayExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final ByteArrayColumn column, final byte[] a) {
    this.update.addSet(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final ObjectColumn column, final ObjectExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

  public UpdateSetPhase set(final ObjectColumn column, final Object o) {
    this.update.addSet(column, BoxUtil.box(o));
    return new UpdateSetPhase(this.update);
  }

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
