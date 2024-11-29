package org.hotrod.runtime.livesql.queries;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.NullLiteral;
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
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class UpdateTablePhase {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateTablePhase(final LiveSQLContext context, final TableOrView tableOrView) {
    this.context = context;
    this.update = new UpdateObject();
    this.update.setTableOrView(tableOrView);
  }

  // Next stages

  public UpdateSetPhase set(final NumberEntityColumn column, final NumberExpression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumberEntityColumn column, final Number n) {
    this.update.addSet(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumberEntityColumn column, final NullLiteral n) {
    this.update.addSet(column, n);
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

  public UpdateSetPhase set(final StringEntityColumn column, final NullLiteral n) {
    this.update.addSet(column, n);
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

  public UpdateSetPhase set(final DateTimeEntityColumn column, final NullLiteral n) {
    this.update.addSet(column, n);
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

  public UpdateSetPhase set(final BooleanEntityColumn column, final NullLiteral n) {
    this.update.addSet(column, n);
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

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final NullLiteral n) {
    this.update.addSet(column, n);
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

  public UpdateSetPhase set(final ObjectEntityColumn column, final NullLiteral n) {
    this.update.addSet(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

}
